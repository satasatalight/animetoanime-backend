package li.sata.animetoanime;

//import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.jaikanmodels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
import pw.mihou.jaikan.models.Anime;

@SpringBootTest
class AnimetoanimeApplicationTests {
	JaikanRepository repo = new JaikanRepository();
	int animeid = 12403;
	int staffid = 504;

	@Test
	void defaultJaikanEndPointTest(){
		Anime first = null;

		try{
			List<Anime> yuruSearch = Jaikan.list(Endpoints.SEARCH, Anime.class, "anime", "Yuru Yuri").get();
			first = yuruSearch.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);
		assertEquals(first.title, "Yuru Yuri♪♪");
		assertEquals(first.status, "Finished Airing");

//		System.out.println(first.title);
//		System.out.println(first.status);
	}

	@Test
	void staffEndpointTest(){
		JaikanStaff first = null;

		try{
			List<JaikanStaff> yuruStaff = Jaikan.list(repo.animeStaffEndpoint, JaikanStaff.class, animeid).get();
			first = yuruStaff.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);
		assertEquals(first.person.name, "Kamata, Hajime");
		assertEquals(first.positions.get(0), "Producer");

//		System.out.println(first.person.name);
//		System.out.println(first.positions.get(0));
	}

	@Test
	void charEndpointTest(){
		JaikanCharacter first = null;

		try{
			List<JaikanCharacter> yuruChars = Jaikan.list(repo.animeCharacterEndpoint, JaikanCharacter.class, animeid).get();
			first = yuruChars.get(0);
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(first == null);
		assertEquals(first.character.name, "Akaza, Akari");
		assertEquals(first.role, "Main");

//		System.out.println(first.character.name);
//		System.out.println(first.role);
	}

	@Test
	void randomEndpointTest(){
		Anime rand = null;

		try{
			rand = Jaikan.object(repo.randomEndpoint, Anime.class).get();
		}

		catch(Exception e) {
			e.printStackTrace(System.err);
		}

		assertFalse(rand == null);

//		System.out.println(rand.title);
	}

	@Test
	void jaikanStaffTest() {
		List<Staff> staff = repo.getAnimeStaff(animeid);
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
		List<li.sata.animetoanime.genericmodels.Anime> anime = repo.getStaffAnime(staffid);
		assertFalse(anime == null);
		assertFalse(anime.size() == 0);

		//for(li.sata.animetoanime.genericmodels.Anime a : anime) {
		//	System.out.println(a.title);
		//	System.out.println("\t" + a.role);
		//	System.out.println("\t" + a.imageUrl);
		//	System.out.println("\t" + a.id);
		//	System.out.println();
		//}
	}
}
