package li.sata.animetoanime.repomodels;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;

@Entity
public class AnimeList {
    @Id
    public int id; // unique id corresponding to staff 

    // list of anime the staff member worked on
    @OneToMany(fetch = FetchType.LAZY)
    public List<Anime> animeList = new ArrayList<>();

    AnimeList(Staff a, List<Anime> l){
        this.id = a.id;
        this.animeList = l;
    }

    public AnimeList(){}
}
