package li.sata.animetoanime.genericmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public int dataBaseId; // auto-generated unique id for database purposes, not related to id from Jaikan

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
