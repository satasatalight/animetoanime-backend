package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;
import li.sata.animetoanime.jaikanmodels.JaikanVoiceActor;

public class Staff {
    public String name;
    public List<String> positions;
    public String imageUrl;
    public int id;

    Staff() {}

    public Staff(JaikanStaff jaikanStaff) {
        this.name = jaikanStaff.person.name;
        this.positions = jaikanStaff.positions;
        this.imageUrl = jaikanStaff.person.images.firstDefault();
        this.id = jaikanStaff.person.id;
    }

    public static List<Staff> fromJaikanStaff(List<JaikanStaff> jaikanStaff) {
        List<Staff> staffList = new ArrayList<>();
        for (JaikanStaff staff : jaikanStaff) {
            staffList.add(new Staff(staff));
        }
        return staffList;
    }

    // extract a full list of voice actors from a character list (one voice actor can voice multiple characters in the same anime)
    public static List<Staff> fromJaikanCharacters(List<JaikanCharacter> jaikanCharacters) {
        HashMap<Integer, VoiceActor> voiceActorMap = new HashMap<>(); // malid -> VoiceActor pairs
        for(JaikanCharacter jaikanCharacter : jaikanCharacters) {
            fromJaikanCharacter(jaikanCharacter, voiceActorMap);
        }
        return new ArrayList<>(voiceActorMap.values());
    }

    // add all VA's from one character into actor map
    static void fromJaikanCharacter(JaikanCharacter jaikanCharacter, HashMap<Integer, VoiceActor> voiceActorMap) {
        for(JaikanVoiceActor jaikanVoiceActor : jaikanCharacter.voiceActors) {
            VoiceActor actor = voiceActorMap.getOrDefault(jaikanVoiceActor.person.id, new VoiceActor(jaikanVoiceActor));
            actor.positions.add("Voices " + jaikanCharacter.character.name + " in " + jaikanVoiceActor.language);
            actor.characters.add(new Character(jaikanCharacter.character.name, jaikanCharacter.character.images.firstDefault()));
            voiceActorMap.put(jaikanVoiceActor.person.id, actor);
        }
    }

    public boolean equals(Staff other) {
        return this.id == other.id;
    }
}
