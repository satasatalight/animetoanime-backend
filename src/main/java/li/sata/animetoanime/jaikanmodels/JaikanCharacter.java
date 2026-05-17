package li.sata.animetoanime.jaikanmodels;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class JaikanCharacter {
    public JaikanPerson character;
    public String role;
    public int favorites;
    @SerializedName("voice_actors")
    public List<JaikanVoiceActor> voiceActors; 
}
