package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanVoiceActor;

public class VoiceActor extends Staff {
    List<Character> characters;

    VoiceActor(JaikanVoiceActor jaikanVoiceActor) {
        characters = new ArrayList<>();
        this.name = jaikanVoiceActor.person.name;
        this.imageUrl = jaikanVoiceActor.person.images.firstDefault();
        this.id = jaikanVoiceActor.person.id;
    }
}
