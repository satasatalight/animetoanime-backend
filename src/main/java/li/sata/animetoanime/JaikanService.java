package li.sata.animetoanime;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.jaikanmodels.JaikanAnimePosition;
import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;
import li.sata.animetoanime.jaikanmodels.JaikanVoicePosition;
import pw.mihou.jaikan.Jaikan;
// import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.endpoints.Endpoint;
import pw.mihou.jaikan.endpoints.Endpoints;

@Service
public class JaikanService implements AnimeService {
    //Endpoint animeStaffEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/staff");
    //Endpoint animeCharacterEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/anime/{}/characters");
    //Endpoint staffAnimeEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/anime");
    //Endpoint staffVoicesEndpoint = Endpoints.createEndpoint("https://api.jikan.moe/v4/people/{}/voices");

    Endpoint animeStaffEndpoint = Endpoints.createEndpoint("https://api.tenrai.org/v1/anime/{}/staff");
    Endpoint animeCharacterEndpoint = Endpoints.createEndpoint("https://api.tenrai.org/v1/anime/{}/characters");
    Endpoint staffAnimeEndpoint = Endpoints.createEndpoint("https://api.tenrai.org/v1/people/{}/anime");
    Endpoint staffVoicesEndpoint = Endpoints.createEndpoint("https://api.tenrai.org/v1/people/{}/voices");
    Endpoint objectEndpoint = Endpoints.createEndpoint("https://api.tenrai.org/v1/{}/{}");

    // get list of top 1000s anime on MAL (as of June 2026)
    ClassPathResource topList = new ClassPathResource("top.txt");
    ArrayList<String> topAnimeIDs = new ArrayList<>();

    JaikanService() {
        // fill topAnimeID list object
        try(Scanner scan = new Scanner(topList.getFile())){
            while(scan.hasNextLine()) {
                String id = scan.nextLine();
                topAnimeIDs.add(id);
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }

        Jaikan.setConfiguration(builder -> builder
            .setRatelimit(Duration.ofMillis(800))
            .build());
    }

    // pull random anime from top 1000 list
    public Anime getRandomAnime() {
        Anime rand = null;

        Random r = new Random();
        String randomId = topAnimeIDs.get(r.nextInt(topAnimeIDs.size()));

        try {
            rand = new Anime(Jaikan.object(objectEndpoint, pw.mihou.jaikan.models.Anime.class, "anime", randomId).get());
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return rand;
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
        List<JaikanAnimePosition> jaikanAnimePositionList = null;
        List<JaikanVoicePosition> jaikanVoicePositionList = null;

        try {
            jaikanAnimePositionList = Jaikan.list(staffAnimeEndpoint, JaikanAnimePosition.class, malid).get();
            jaikanVoicePositionList = Jaikan.list(staffVoicesEndpoint, JaikanVoicePosition.class, malid).get();
        } 

        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        // combine overlapping voice and staff roles for anime
        HashMap<Integer, Anime> animeMap = new HashMap<>();
        Anime.fromJaikanAnimePositions(jaikanAnimePositionList, animeMap);
        Anime.fromJaikanVoicePositions(jaikanVoicePositionList, animeMap);

        return new ArrayList<>(animeMap.values());
    }
}
