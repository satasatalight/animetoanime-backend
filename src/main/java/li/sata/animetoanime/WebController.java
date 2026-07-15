package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.repomodels.DailyData;

@RestController
public class WebController {
    AnimeToAnimeService service;
    
    private String cronPassword = System.getenv("A2A_CRON_PASSWORD");

    WebController(AnimeToAnimeService s){
        service = s;
    }

    @CrossOrigin
    @GetMapping("/getAnimeStaff")
    public List<Staff> getAnimeStaff(@RequestParam int id) {
        return service.getAnimeStaff(id);
    }
    
    @CrossOrigin
    @GetMapping("/getStaffAnime")
    public List<Anime> getStaffAnime(@RequestParam int id) {
        return service.getStaffAnime(id);
    }
    
    @CrossOrigin
    @GetMapping("/getDailyGame")
    public DailyData getDailyGame(@RequestParam LocalDate date) {
        return service.getDailyData(date);
    }
    
    @GetMapping("/cron")
    public ResponseEntity<?> generateNewDailyGame(
        @RequestHeader(value="authorization") String authorization, @RequestParam(required = false) LocalDate date) {
        if(authorization.equals(cronPassword)) {
            if(date != null)
                CompletableFuture.runAsync(() -> service.generateDailyData(date));
            else
                CompletableFuture.runAsync(() -> service.generateDailyData());

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
