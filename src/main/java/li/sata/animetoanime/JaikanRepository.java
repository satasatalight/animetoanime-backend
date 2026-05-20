package li.sata.animetoanime;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.jaikanmodels.JaikanAnimePosition;
import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;
import li.sata.animetoanime.jaikanmodels.JaikanVoicePosition;
import pw.mihou.jaikan.Jaikan;
// import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.endpoints.Endpoint;
import pw.mihou.jaikan.endpoints.Endpoints;

@Repository
public class JaikanRepository implements AnimeRepository {
    // list of ids that returned 500 http codes from jaikan
    public static ArrayList<Integer> skippedIds= new ArrayList<>();

    Endpoint animeStaffEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/staff");
    Endpoint animeCharacterEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/characters");
    Endpoint staffAnimeEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/anime");
    Endpoint staffVoicesEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/voices");
    Endpoint randomEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/random/anime?sfw");

    JaikanRepository() {
        Jaikan.setConfiguration(builder -> builder
            .setRatelimit(Duration.ofMillis(500))
            .build());
    }

    public Anime getRandomAnime() {
        Anime anime = null;

        try {
            pw.mihou.jaikan.models.Anime jaikanAnime = Jaikan.object(randomEndpoint, pw.mihou.jaikan.models.Anime.class).get();
            anime = new Anime(jaikanAnime);
        } 

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return anime;
    }

    public List<Staff> getAnimeStaff(int malid) {
        List<JaikanStaff> jaikanStaffList = null;
        List<JaikanCharacter> jaikanCharacterList = null;

        try {
            jaikanStaffList = Jaikan.list(animeStaffEndpoint, JaikanStaff.class, malid).get();
            jaikanCharacterList = Jaikan.list(animeCharacterEndpoint, JaikanCharacter.class, malid).get();
        } 

        catch (Exception e) {
            System.out.println(e.getMessage());
            skippedIds.add(malid);
            return new ArrayList<>();
        }

        // staff and character actors can overlap
        // use hashmap to avoid duplicates and merge VA's who also have staff positions
        HashMap<Integer, Staff> staffMap = new HashMap<>();
        Staff.fromJaikanStaff(jaikanStaffList, staffMap);
        Staff.fromJaikanCharacters(jaikanCharacterList, staffMap);

        return new ArrayList<>(staffMap.values());
    }

    public List<Anime> getStaffAnime(int malid) {
        List<JaikanAnimePosition> jaikanAnimePositionList = null;
        List<JaikanVoicePosition> jaikanVoicePositionList = null;

        try {
            jaikanAnimePositionList = Jaikan.list(staffAnimeEndpoint, JaikanAnimePosition.class, malid).get();
            jaikanVoicePositionList = Jaikan.list(staffVoicesEndpoint, JaikanVoicePosition.class, malid).get();
        } 

        catch (Exception e) {
            System.out.println(e.getMessage());
            skippedIds.add(malid);
            return new ArrayList<>();
        }

        // combine overlapping voice and staff roles for anime
        HashMap<Integer, Anime> animeMap = new HashMap<>();
        Anime.fromJaikanAnimePositions(jaikanAnimePositionList, animeMap);
        Anime.fromJaikanVoicePositions(jaikanVoicePositionList, animeMap);

        return new ArrayList<>(animeMap.values());
    }
}
