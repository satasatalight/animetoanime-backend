package li.sata.animetoanime.genericmodels;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(EntryCompositeKey.class)
public abstract class Entry {
    @Id
    @JsonProperty("id")
    public int entryId;

    @Id
    @JsonIgnore
    public int sourceId; 

    @CreationTimestamp
    @JsonIgnore
    public LocalDate creation;

    public String name;
    public String imageUrl;

    Entry() {}

    public boolean equals(Entry other) {
        return this.entryId == other.entryId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            return equals((Entry) obj);
        }
        return super.equals(obj);
    }
}
