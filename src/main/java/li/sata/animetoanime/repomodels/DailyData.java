package li.sata.animetoanime.repomodels;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;

@Entity
public class DailyData {
    @Id
    public LocalDate date;

    public String name;

    @OneToOne(cascade = CascadeType.ALL)
    public Anime anime1;

    @OneToOne(cascade = CascadeType.ALL)
    public Anime anime2;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Entry> shortestPath;

    // set to current time from UTC+14 (farthest time zone in the future)
    public DailyData (){
        date = currentWorkingDate();
    }

    public DailyData (LocalDate d){
        date = d;
    }

    public DailyData(Anime a1, Anime a2, List<Entry> path){
        anime1 = a1;
        anime2 = a2;
        shortestPath = path;
        date = currentWorkingDate();
    }

    public DailyData(Anime a1, Anime a2, List<Entry> path, LocalDate d){
        anime1 = a1;
        anime2 = a2;
        shortestPath = path;
        date = d;
    }

    public static LocalDate currentWorkingDate(){
        return LocalDate.from(ZonedDateTime.now(ZoneId.of("UTC+14:00"))).plusDays(1);
    }
}
