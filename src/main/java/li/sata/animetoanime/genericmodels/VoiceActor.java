package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanVoiceActor;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class VoiceActor extends Staff {
    @Embedded
    List<Character> characters;

    VoiceActor(Staff staff) {
        this.name = staff.name;
        this.positions = staff.positions;
        this.imageUrl = staff.imageUrl;
        this.id = staff.id;
        this.characters = new ArrayList<>();
    }

    VoiceActor(JaikanVoiceActor jaikanVoiceActor) {
        this.name = jaikanVoiceActor.person.name;
        this.positions = new ArrayList<>();
        this.imageUrl = jaikanVoiceActor.person.images.firstDefault();
        this.id = jaikanVoiceActor.person.id;
        this.characters = new ArrayList<>();
    }
}
