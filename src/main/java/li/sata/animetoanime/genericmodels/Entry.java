package li.sata.animetoanime.genericmodels;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@MappedSuperclass
public abstract class Entry {
    public String name;
    public String imageUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    Entry() {}

    public boolean equals(Entry other) {
        return this.id == other.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            return equals((Entry) obj);
        }
        return super.equals(obj);
    }
}
