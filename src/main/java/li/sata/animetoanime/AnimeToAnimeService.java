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
        LinkedList<Entry> queue = new LinkedList<>();
        HashMap<Integer, Entry> parentMap = new HashMap<>(); // use parent map for checking visited and reconstructing path

        parentMap.put(start.id, null);
        queue.add(start);

        while(!queue.isEmpty()){
            Entry current = queue.poll();

            System.out.println("Visiting: " + current.name);

            if(current.equals(end))
                return reconstructPath(parentMap, current);

            List<Entry> children = new ArrayList<>();

            // get appropriate children based on type of current entry
            if(current instanceof Anime){
                children.addAll(repo.getAnimeStaff(current.id));
            } 

            else if(current instanceof Staff){
                children.addAll(repo.getStaffAnime(current.id));
            }

            // peek for end in children to avoid adding unnecessary nodes to the queue
            if(current instanceof Staff && children.contains(end)){
                parentMap.put(end.id, current);
                return reconstructPath(parentMap, end);
            }

            for(Entry child : children){
                if(!parentMap.containsKey(child.id)){
                    parentMap.put(child.id, current);

                    if(child instanceof Anime) {
                        queue.addLast(child);
                    } 
                    
                    else if(child instanceof Staff) {
                        queue.addFirst(child);
                    }
                }
            }
        }

        return null;
    }

    private List<Entry> reconstructPath(HashMap<Integer, Entry> parentMap, Entry end){
        List<Entry> path = new ArrayList<>();
        Entry current = end;

        while(current != null){
            path.add(0, current); // add to the front of the list
            current = parentMap.get(current.id);
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
