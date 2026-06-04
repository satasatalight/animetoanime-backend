package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanAnimePosition;
import li.sata.animetoanime.jaikanmodels.JaikanVoicePosition;
import jakarta.persistence.Embeddable;

@Embeddable
public class Anime extends Entry {
    public List<String> role;

    public Anime() {}

    public Anime(JaikanAnimePosition jaikanAnimePosition) {
        this.name = jaikanAnimePosition.anime.title;
        this.imageUrl = jaikanAnimePosition.anime.images.firstDefault();
        this.role = new ArrayList<>();
        this.id = jaikanAnimePosition.anime.id;
    }

    public Anime(JaikanVoicePosition jaikanVoicePosition) {
        this.name = jaikanVoicePosition.anime.title;
        this.imageUrl = jaikanVoicePosition.anime.images.firstDefault();
        this.role = new ArrayList<>();
        this.id = jaikanVoicePosition.anime.id;
    }

    public Anime(pw.mihou.jaikan.models.Anime jaikanAnime) {
        this.name = jaikanAnime.title;
        this.imageUrl = jaikanAnime.images.firstDefault();
        this.role = new ArrayList<>();
        this.id = jaikanAnime.id;
    }

    public static void fromJaikanAnimePositions(List<JaikanAnimePosition> jaikanAnimePositions, HashMap<Integer, Anime> animeMap) {
        for (JaikanAnimePosition animePosition : jaikanAnimePositions){
            Anime anime = animeMap.getOrDefault(animePosition.anime.id, new Anime(animePosition));
            anime.role.add(animePosition.position);
            animeMap.put(animePosition.anime.id, anime);
        }
    }

    public static void fromJaikanVoicePositions(List<JaikanVoicePosition> jaikanVoicePositions, HashMap<Integer, Anime> animeMap) {
        for (JaikanVoicePosition voicePosition : jaikanVoicePositions){
            Anime anime = animeMap.getOrDefault(voicePosition.anime.id, new Anime(voicePosition));
            anime.role.add(voicePosition.role + " - Voices " + voicePosition.character.name);
            animeMap.put(voicePosition.anime.id, anime);
        }
    }
}
