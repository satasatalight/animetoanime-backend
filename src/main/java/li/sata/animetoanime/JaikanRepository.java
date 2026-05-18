package li.sata.animetoanime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;

import pw.mihou.jaikan.Jaikan;
// import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.endpoints.Endpoint;
import pw.mihou.jaikan.endpoints.Endpoints;

@Repository
public class JaikanRepository {
    Endpoint animeStaffEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/staff");
    Endpoint animeCharacterEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/characters");
    Endpoint staffAnimeEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/anime");
    Endpoint staffVoicesEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/voices");
    Endpoint randomEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/random/anime?sfw");

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
            e.printStackTrace();
            return null;
        }

        // staff and character actors can overlap
        // use hashmap to avoid duplicates and merge VA's who also have staff positions
        HashMap<Integer, Staff> staffMap = new HashMap<>();
        Staff.fromJaikanStaff(jaikanStaffList, staffMap);
        Staff.fromJaikanCharacters(jaikanCharacterList, staffMap);

        return new ArrayList<>(staffMap.values());
    }

    public List<Anime> getStaffAnime(int malid) {
        return null;
    }
}
