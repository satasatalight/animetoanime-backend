package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.repomodels.DailyData;

@RestController
public class WebController {
    AnimeToAnimeService service;

    WebController(AnimeToAnimeService s){
        service = s;
    }

    @GetMapping("/getAnimeStaff")
    public List<Staff> getAnimeStaff(@RequestParam int id) {
        return service.getAnimeStaff(id);
    }
    
    @GetMapping("/getStaffAnime")
    public List<Anime> getStaffAnime(@RequestParam int id) {
        return service.getStaffAnime(id);
    }
    
    @GetMapping("/getDailyGame")
    public DailyData getDailyGame(@RequestParam LocalDate date) {
        return service.getDailyData(date);
    }
}
