package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanVoiceActor;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
public class VoiceActor extends Staff {
    @ElementCollection
    public List<Character> characters;

    VoiceActor() {}

    VoiceActor(Staff staff) {
        this.name = staff.name;
        this.positions = staff.positions;
        this.imageUrl = staff.imageUrl;
        this.entryId = staff.entryId;
        this.sourceId = staff.sourceId;
        this.characters = new ArrayList<>();
    }

    VoiceActor(JaikanVoiceActor jaikanVoiceActor, int sourceId) {
        this.name = jaikanVoiceActor.person.name;
        this.positions = new ArrayList<>();
        this.imageUrl = jaikanVoiceActor.person.images.firstDefault();
        this.entryId = jaikanVoiceActor.person.id;
        this.sourceId = sourceId;
        this.characters = new ArrayList<>();
    }
}
