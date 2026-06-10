package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public void generateDailyData(){
        // get two random anime

        // calculate shortest path 
        
        // store in database
    }

    public List<Entry> findShortestPath(Anime anime1, Anime anime2){
        // get staff lists for both anime
		List<Staff> staffList1 = getAnimeStaff(anime1.id);
		List<Staff> staffList2 = getAnimeStaff(anime2.id);

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

        animeParentMap.put(start.id, null);
        animeQueue.add(start);

        while(!animeQueue.isEmpty()){
            Anime currAnime = animeQueue.poll();
            List<Staff> animeStaff = getAnimeStaff(currAnime.id);

            System.out.println("Visting Anime: " + currAnime.name);

            // jaikan returned a 500 http error:
            // add anime to the back of the queue to try again later
            if(animeStaff == null){
                animeQueue.add(currAnime);
                continue;
            }

            Queue<Staff> staffQueue = new LinkedList<>();

            // fill staff queue with current anime's staff list
            for(Staff staffMember : animeStaff){
                // skip already visited staff
                if(staffParentMap.containsKey(staffMember.id))
                    continue;
                
                // add curAnime as parent and place in queue
                staffParentMap.put(staffMember.id, currAnime);
                staffQueue.add(staffMember);
            }

            int queueLength = staffQueue.size();

            // check each staff member for connections to final series
            for(int i = 0; !staffQueue.isEmpty(); i++){
                Staff curStaff = staffQueue.poll();

                System.out.println("Visting Staff: " + curStaff.name);

                List<Anime> staffAnimes = getStaffAnime(curStaff.id);

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
                    animeParentMap.put(end.id, curStaff);
                    return reconstructPath(animeParentMap, staffParentMap, end);
                }

                // otherwise, 
                // add all anime to the queue to explore next
                for(Anime staffAnime : staffAnimes){
                    // skip already explored anime
                    if(animeParentMap.containsKey(staffAnime.id))
                        continue;

                    // add curstaff as parent and place in queue
                    animeParentMap.put(staffAnime.id, curStaff);
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
                current = animeMap.get(current.id);
            
            else
                current = staffMap.get(current.id);
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

            if(res != null)
                animeRepo.save(new StaffList(id, res));
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

            if(res != null)
                staffRepo.save(new AnimeList(id, res));
        }

        return res;
    }

    public void addToAverageScore(int score){

    }
}
