package li.sata.animetoanime.genericmodels;

import jakarta.persistence.MappedSuperclass;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;

@MappedSuperclass
public abstract class Entry {
    public String name;
    public String imageUrl;

    @AttributeOverride(name = "id", column = @Column(name = "entry_id"))
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
