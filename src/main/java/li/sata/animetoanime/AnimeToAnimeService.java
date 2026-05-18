package li.sata.animetoanime;

import java.util.List;

import org.springframework.stereotype.Service;

import li.sata.animetoanime.genericmodels.Anime;
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

    private void calculateShortestPath(int id1, int id2){

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
