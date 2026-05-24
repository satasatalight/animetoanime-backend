package li.sata.animetoanime;

import java.util.List;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;

public interface AnimeService {
    // return random sfw anime
    public Anime getRandomAnime();

    // return complete list of staff on anime with malid
    public List<Staff> getAnimeStaff(int malid);

    // return complete list of anime a staff with malid has worked on
    public List<Anime> getStaffAnime(int malid);
}
