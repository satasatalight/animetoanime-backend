package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.repomodels.AnimeList;
import li.sata.animetoanime.repomodels.DailyData;
import li.sata.animetoanime.repomodels.StaffList;

@Service
public class AnimeToAnimeService {
    AnimeService animeService;
    AnimeRepository animeRepo;
    StaffRepository staffRepo;
    DailyDataRepository dailyRepo;

    public AnimeToAnimeService(AnimeService service, AnimeRepository a, StaffRepository s, DailyDataRepository d) {
      this.animeService = service;
      this.animeRepo = a;
      this.staffRepo = s;
      this.dailyRepo = d;
    }

    public void generateDailyData() {
        generateDailyData(new DailyData());
    }

    public void generateDailyData(LocalDate d) {
        generateDailyData(new DailyData(d));
    }

    public void generateDailyData(DailyData nextDailyData){
        System.out.println("Generating for " + nextDailyData.date);

        // delete two day old dailydata
        dailyRepo.deleteByDateLessThan(DailyData.currentWorkingDate().minusDays(2));
        
        // clear two day old cache in anime and staff repos
        animeRepo.deleteByCreationLessThan(LocalDate.now().minusDays(2));
        staffRepo.deleteByCreationLessThan(LocalDate.now().minusDays(2));

        // get two random anime
        Anime anime1 = animeService.getRandomAnime();
        Anime anime2 = animeService.getRandomAnime();

        System.out.println("got " + anime1.name + " and " + anime2.name);

        // calculate shortest path 
        List<Entry> shortestPath = this.findShortestPath(anime1, anime2);

        for(Entry e : shortestPath)
            System.out.println(e.name + " -> ");
        
        // save to dailydata object
        nextDailyData.anime1 = anime1;
        nextDailyData.anime2 = anime2;
        nextDailyData.shortestPath = shortestPath;

        // store in database
        dailyRepo.save(nextDailyData);
    }

    public List<Entry> findShortestPath(Anime anime1, Anime anime2){
        // get staff lists for both anime
		List<Staff> staffList1 = getAnimeStaff(anime1.entryId);
		List<Staff> staffList2 = getAnimeStaff(anime2.entryId);

        // if both are unreachable, give up on search
        if(staffList1 == null && staffList2 == null)
            return null;

        // start at reachable anime
        else if(staffList1 == null)
            return searchShortestPath(anime2, anime1);
        else if(staffList2 == null)
            return searchShortestPath(anime1, anime2);

        // if both are reachable,
        else{
            // check for an overlapping staff member
            List<Staff> intersection = new ArrayList<>(staffList1);
            intersection.retainAll(staffList2);

            // shortcut answer if both staff lists contain a matching member
            if(!intersection.isEmpty())
                return Arrays.asList(anime1, intersection.get(0), anime2);

            // otherwise, start at smaller list
            if(staffList2.size() > staffList1.size())
                return searchShortestPath(anime1, anime2);
            else 
                return searchShortestPath(anime2, anime1);
        }
    }

    private List<Entry> searchShortestPath(Anime start, Anime end){
        Queue<Anime> animeQueue = new LinkedList<>();

        HashMap<Integer, Staff> animeParentMap = new HashMap<>(); 
        HashMap<Integer, Anime> staffParentMap = new HashMap<>();

        animeParentMap.put(start.entryId, null);
        animeQueue.add(start);

        while(!animeQueue.isEmpty()){
            Anime curAnime = animeQueue.poll();

            System.out.println("Visting Anime: " + curAnime.name);

            List<Staff> animeStaff = getAnimeStaff(curAnime.entryId);

            // jaikan returned a 500 http error:
            // add anime to the back of the queue to try again later
            if(animeStaff == null){
                animeQueue.add(curAnime);
                continue;
            }

            Queue<Staff> staffQueue = new LinkedList<>();

            // fill staff queue with current anime's staff list
            for(Staff staffMember : animeStaff){
                // skip already visited staff
                if(staffParentMap.containsKey(staffMember.entryId))
                    continue;
                
                // add curAnime as parent and place in queue
                staffParentMap.put(staffMember.entryId, curAnime);
                staffQueue.add(staffMember);
            }

            int queueLength = staffQueue.size();

            // check each staff member for connections to final series
            for(int i = 0; !staffQueue.isEmpty(); i++){
                Staff curStaff = staffQueue.poll();

                System.out.println("Visting Staff: " + curStaff.name);

                List<Anime> staffAnimes = getStaffAnime(curStaff.entryId);

                // if repo returned an error (null value), 
                if(staffAnimes == null){
                    // if we're going thru the queue for the first time,
                    // put staff to the back of the queue to try again later
                    if(i < queueLength)
                        staffQueue.add(curStaff);
                    
                    // otherwise, skip entry
                    continue;
                }

                // if end anime is in staff list, 
                // connect and return path
                if(staffAnimes.contains(end)){
                    animeParentMap.put(end.entryId, curStaff);
                    return reconstructPath(animeParentMap, staffParentMap, end);
                }

                // otherwise, 
                // add all anime to the queue to explore next
                for(Anime staffAnime : staffAnimes){
                    // skip already explored anime
                    if(animeParentMap.containsKey(staffAnime.entryId))
                        continue;

                    // add curstaff as parent and place in queue
                    animeParentMap.put(staffAnime.entryId, curStaff);
                    animeQueue.add(staffAnime);
                }
            }
        }

        return null;
    }

    private List<Entry> reconstructPath(HashMap<Integer, Staff> animeMap, HashMap<Integer, Anime> staffMap, Anime end){
        List<Entry> path = new ArrayList<>();
        Entry current = end;

        while(current != null){
            path.add(0, current);

            if(current instanceof Anime)
                current = animeMap.get(current.entryId);
            
            else
                current = staffMap.get(current.entryId);
        }

        return path;
    }

    public DailyData getDailyData(LocalDate date){
        return dailyRepo.findById(date).orElse(null);
    }

    public List<Staff> getAnimeStaff(int id){
        List<Staff> res = null;
        StaffList fromRepo = animeRepo.findById(id).orElse(null);

        if(fromRepo != null)
            res = fromRepo.staffList;
        
        else {
            res = animeService.getAnimeStaff(id);

            final List<Staff> finalRes = res;
            if(res != null)
                CompletableFuture.runAsync(() -> animeRepo.save(new StaffList(id, finalRes)));
        }

        return res;
    }

    public List<Anime> getStaffAnime(int id){
        List<Anime> res = null;
        AnimeList fromRepo = staffRepo.findById(id).orElse(null);

        if(fromRepo != null)
            res = fromRepo.animeList;

        else{
            res = animeService.getStaffAnime(id);

            final List<Anime> finalRes = res;
            if(res != null)
                CompletableFuture.runAsync(() -> staffRepo.save(new AnimeList(id, finalRes)));
        }

        return res;
    }

    public void addToAverageScore(int score){

    }
}
