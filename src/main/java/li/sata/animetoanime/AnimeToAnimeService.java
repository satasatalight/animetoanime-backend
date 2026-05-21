package li.sata.animetoanime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Service;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.genericmodels.Staff;

@Service
public class AnimeToAnimeService {
    AnimeRepository repo;

    public AnimeToAnimeService(AnimeRepository repo) {
        this.repo = repo;
    }

    public void generateDailyData(){
        // get two random anime

        // calculate shortest path 
        
        // store in upstash
    }

    public List<Entry> calculateShortestPath(Anime start, Anime end){
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
                    // if we're still going thru the queue for the first time,
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
                // add all anime to the queue to get explored next
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

    public List<Anime> getDailyData(){
        return null;
    }

    public List<Staff> getAnimeStaff(int id){
        return repo.getAnimeStaff(id);
    }

    public List<Anime> getStaffAnime(int id){
        return repo.getStaffAnime(id);
    }

    public int getShortestPath(){
        return 0;
    }

    public void addToAverageScore(int score){

    }
}
