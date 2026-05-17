package li.sata.animetoanime;

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
    Endpoint staffEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/staff");
    Endpoint characterEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/characters");
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
            jaikanStaffList = Jaikan.list(staffEndpoint, JaikanStaff.class, malid).get();
            jaikanCharacterList = Jaikan.list(characterEndpoint, JaikanCharacter.class, malid).get();
        } 

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // combine staff list and voice actor's list
        // maybe keep separate ?
        List<Staff> staffList = Staff.fromJaikanStaff(jaikanStaffList);
        staffList.addAll(Staff.fromJaikanCharacters(jaikanCharacterList));

        return staffList;
    }

    public List<Anime> getStaffAnime(int malid) {
        return null;
    }
}
