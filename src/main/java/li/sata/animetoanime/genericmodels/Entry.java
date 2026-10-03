package li.sata.animetoanime.genericmodels;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

// entry ids are duplicated across anime and staff
// using a combined key allows for more intuitive retrival 
// Ex. "Get anime {entryId} that staff {sourceId} worked on"
class EntryCompositeKey implements Serializable {
    public int entryId;
    public int sourceId;
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
