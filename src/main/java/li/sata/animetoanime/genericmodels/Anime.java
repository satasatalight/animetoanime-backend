package li.sata.animetoanime.genericmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import li.sata.animetoanime.jaikanmodels.JaikanAnimePosition;
import li.sata.animetoanime.jaikanmodels.JaikanVoicePosition;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
public class Anime extends Entry {
    @ElementCollection
    @Column(length = 500)
    public List<String> role = new ArrayList<>();

    public Anime() {}

    public Anime(JaikanAnimePosition jaikanAnimePosition, int sourceId) {
        this.name = jaikanAnimePosition.anime.title;
        this.imageUrl = jaikanAnimePosition.anime.images.firstDefault();
        this.role = new ArrayList<>();
        this.entryId = jaikanAnimePosition.anime.id;
        this.sourceId = sourceId;
    }

    public Anime(JaikanVoicePosition jaikanVoicePosition, int sourceId) {
        this.name = jaikanVoicePosition.anime.title;
        this.imageUrl = jaikanVoicePosition.anime.images.firstDefault();
        this.role = new ArrayList<>();
        this.entryId = jaikanVoicePosition.anime.id;
        this.sourceId = sourceId;
    }

    public Anime(pw.mihou.jaikan.models.Anime jaikanAnime, int sourceId) {
        this.name = jaikanAnime.title;
        this.imageUrl = jaikanAnime.images.firstDefault();
        this.role = new ArrayList<>();
        this.entryId = jaikanAnime.id;
        this.sourceId = sourceId;
    }

    public static void fromJaikanAnimePositions(List<JaikanAnimePosition> jaikanAnimePositions, HashMap<Integer, Anime> animeMap, int sourceId) {
        for (JaikanAnimePosition animePosition : jaikanAnimePositions){
            Anime anime = animeMap.getOrDefault(animePosition.anime.id, new Anime(animePosition, sourceId));
            anime.role.add(animePosition.position);
            animeMap.put(animePosition.anime.id, anime);
        }
    }

    public static void fromJaikanVoicePositions(List<JaikanVoicePosition> jaikanVoicePositions, HashMap<Integer, Anime> animeMap, int sourceId) {
        for (JaikanVoicePosition voicePosition : jaikanVoicePositions){
            Anime anime = animeMap.getOrDefault(voicePosition.anime.id, new Anime(voicePosition, sourceId));
            anime.role.add(voicePosition.role + " - Voices " + voicePosition.character.name);
            animeMap.put(voicePosition.anime.id, anime);
        }
    }
}
