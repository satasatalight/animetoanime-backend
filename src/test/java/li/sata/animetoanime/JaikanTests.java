package li.sata.animetoanime;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.jaikanmodels.JaikanCharacter;
import li.sata.animetoanime.jaikanmodels.JaikanStaff;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
//import pw.mihou.jaikan.models.Anime;

@SpringBootTest
@ActiveProfiles("dev")
class JaikanTests {
	@Autowired
	JaikanService service;

	int animeid = 12403;
	int staffid = 203;

	@Test
	void defaultJaikanEndPointTest(){
		pw.mihou.jaikan.models.Anime first = null;

		try{
			List<pw.mihou.jaikan.models.Anime> yuruSearch = Jaikan.list(Endpoints.SEARCH, pw.mihou.jaikan.models.Anime.class, "anime", "Yuru Yuri").get();
			first = yuruSearch.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);

//		System.out.println(first.title);
//		System.out.println(first.status);
	}

	@Test
	void staffEndpointTest(){
		JaikanStaff first = null;

		try{
			List<JaikanStaff> yuruStaff = Jaikan.list(service.animeStaffEndpoint, JaikanStaff.class, animeid).get();
			first = yuruStaff.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);

//		System.out.println(first.person.name);
//		System.out.println(first.positions.get(0));
	}

	@Test
	void charEndpointTest(){
		JaikanCharacter first = null;

		try{
			List<JaikanCharacter> yuruChars = Jaikan.list(service.animeCharacterEndpoint, JaikanCharacter.class, animeid).get();
			first = yuruChars.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);

//		System.out.println(first.character.name);
//		System.out.println(first.role);
	}

	@Test
	void jaikanStaffTest() {
		List<Staff> staff = service.getAnimeStaff(animeid);
		assertFalse(staff == null);
		assertFalse(staff.size() == 0);

		//for(Staff s : staff) {
		//	System.out.println(s.name);
		//	System.out.println("\t" + s.positions);
		//	System.out.println("\t" + s.imageUrl);
		//	System.out.println("\t" + s.id);
		//	System.out.println();
		//}
	}

	@Test
	void jaikanAnimeTest() {
		List<Anime> anime = service.getStaffAnime(staffid);
		assertFalse(anime == null);
		assertFalse(anime.size() == 0);

		for(li.sata.animetoanime.genericmodels.Anime a : anime) {
			System.out.println(a.name);
			System.out.println("\t" + a.role);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.entryId);
			System.out.println();
		}
	}

	@Test
	void jaikanRandomAnimeTest() {
		Anime anime = service.getRandomAnime();

		System.out.println(anime.name);
		System.out.println(anime.entryId);
		System.out.println(anime.imageUrl);

		assertFalse(anime == null);
	}
}