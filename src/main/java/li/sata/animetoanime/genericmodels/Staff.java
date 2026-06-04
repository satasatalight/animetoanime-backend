package li.sata.animetoanime.genericmodels;

import java.util.HashMap;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;
import li.sata.animetoanime.jaikanmodels.JaikanVoiceActor;
import jakarta.persistence.Embeddable;

@Embeddable
public class Staff extends Entry {
    public List<String> positions;

    Staff() {}

    public Staff(JaikanStaff jaikanStaff) {
        this.name = jaikanStaff.person.name;
        this.positions = jaikanStaff.positions;
        this.imageUrl = jaikanStaff.person.images.firstDefault();
        this.id = jaikanStaff.person.id;
    }

    // place initial list of staff into map
    public static void fromJaikanStaff(List<JaikanStaff> jaikanStaff, HashMap<Integer, Staff> staffMap) {
        for (JaikanStaff staff : jaikanStaff) {
            Staff newStaff = new Staff(staff);
            staffMap.put(staff.person.id, newStaff);
        }
    }

    // extract a full list of voice actors from a character list (one voice actor can voice multiple characters in the same anime)
    public static void fromJaikanCharacters(List<JaikanCharacter> jaikanCharacters, HashMap<Integer, Staff> staffMap) {
        for(JaikanCharacter jaikanCharacter : jaikanCharacters) {
            fromJaikanCharacter(jaikanCharacter, staffMap);
        }
    }

    // merge all VA's from one character into actor map
    static void fromJaikanCharacter(JaikanCharacter jaikanCharacter, HashMap<Integer, Staff> staffMap) {
        for(JaikanVoiceActor jaikanVoiceActor : jaikanCharacter.voiceActors) {
            Staff staff = staffMap.getOrDefault(jaikanVoiceActor.person.id, new VoiceActor(jaikanVoiceActor));
            VoiceActor actor;

            // cast up to VA for existing voice actors
            if(staff instanceof VoiceActor) {
                actor = (VoiceActor) staff;
            } 
            
            // convert staff to VA for staff members who also have voice acting roles
            else {
                actor = new VoiceActor(staff);
            }

            actor.positions.add("Voices " + jaikanCharacter.character.name + " in " + jaikanVoiceActor.language);
            actor.characters.add(new Character(jaikanCharacter.character.name, jaikanCharacter.character.images.firstDefault()));
            staffMap.put(jaikanVoiceActor.person.id, actor);
        }
    }
}
