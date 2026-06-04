package li.sata.animetoanime.repomodels;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;

@Entity
public class AnimeList {
    @Id
    public int id; // unique id corresponding to staff 

    // list of anime the staff member worked on
    @ElementCollection
    @Embedded
    public List<Anime> animeList;

    AnimeList(Staff a, List<Anime> l){
        this.id = a.id;
        this.animeList = l;
    }

    public AnimeList(){}
}
