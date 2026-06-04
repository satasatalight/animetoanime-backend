package li.sata.animetoanime.repomodels;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;

@Entity
public class DailyData {
    @Id
    public LocalDate date;

    public String name;

    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "anime1_entry_id")),
        @AttributeOverride(name = "name", column = @Column(name = "anime1_name")),
        @AttributeOverride(name = "imageUrl", column = @Column(name = "anime1_imageUrl")),
        @AttributeOverride(name = "role", column = @Column(name = "anime1_role")),
    })
    @Embedded
    public Anime anime1;

    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "anime2_entry_id")),
        @AttributeOverride(name = "name", column = @Column(name = "anime2_name")),
        @AttributeOverride(name = "imageUrl", column = @Column(name = "anime2_imageUrl")),
        @AttributeOverride(name = "role", column = @Column(name = "anime2_role")),
    })
    @Embedded
    public Anime anime2;

    @ElementCollection
    @Embedded
    public List<Entry> shortestPath;

    // set to current time from UTC+14 (farthest time zone in the future)
    public DailyData (){
        date = LocalDate.from(ZonedDateTime.now(ZoneId.of("UTC+14:00")));
    }

    public DailyData(Anime a1, Anime a2, List<Entry> path){
        anime1 = a1;
        anime2 = a2;
        shortestPath = path;
        date = LocalDate.from(ZonedDateTime.now(ZoneId.of("UTC+14:00")));
    }
}
