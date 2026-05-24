package li.sata.animetoanime.repomodels;

import java.util.List;

import jakarta.persistence.Entity;
import li.sata.animetoanime.genericmodels.Staff;

@Entity
public class StaffList {
    public int id; // id corresponding to anime
    public List<Staff> staffList;
}
