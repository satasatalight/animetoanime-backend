package li.sata.animetoanime.repomodels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;

@Entity
public class StaffList {
    @Id
    public int id; // unique id corresponding to anime

    @CreationTimestamp
    public LocalDate creation;

    // list of staff involved on anime
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Staff> staffList = new ArrayList<>();

    public StaffList(int a, List<Staff> l){
        this.id = a;
        this.staffList = l;
    }

    public StaffList(Anime a, List<Staff> l){
        this.id = a.id;
        this.staffList = l;
    }

    public StaffList(){}
}
