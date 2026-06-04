package li.sata.animetoanime;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.jaikanmodels.*;
import li.sata.animetoanime.repomodels.DailyData;
import li.sata.animetoanime.repomodels.StaffList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
//import pw.mihou.jaikan.models.Anime;

@SpringBootTest
class JaikanTests {
	JaikanService repo = new JaikanService();
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
		pw.mihou.jaikan.models.Anime rand = null;

		try{
			rand = Jaikan.object(repo.randomEndpoint, pw.mihou.jaikan.models.Anime.class).get();
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
		List<Anime> anime = repo.getStaffAnime(staffid);
		assertFalse(anime == null);
		assertFalse(anime.size() == 0);

		for(li.sata.animetoanime.genericmodels.Anime a : anime) {
			System.out.println(a.name);
			System.out.println("\t" + a.role);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.id);
			System.out.println();
		}
	}
}

@SpringBootTest
class AnimeToAnimeServiceTests {
	Anime anime1 = new Anime();
	Anime anime2 = new Anime();

	AnimeToAnimeServiceTests(){
		anime1.id = 11809;
		anime2.id = 387;
		anime1.name = "gdgd fairies";
		anime2.name = "haibane renmei";
	}

	AnimeToAnimeService service = new AnimeToAnimeService(new JaikanService());

	@Test
	void calculateShortestPathTest() {
		List<Entry> path = service.findShortestPath(anime1, anime2);

		assertFalse(path == null);
		assertFalse(path.size() == 0);

		System.out.println();
		for(Entry e : path) {
			System.out.print(e.name + " -> ");
		}
		System.out.println();
		//System.out.println(JaikanRepository.skippedIds.size() + " cut entries: " + JaikanRepository.skippedIds);
	}
}

@SpringBootTest
class RepoTests {
	Anime anime1 = new Anime();
	Anime anime2 = new Anime();

	RepoTests(){
		anime1.id = 11809;
		anime2.id = 387;
		anime1.name = "gdgd fairies";
		anime2.name = "Haibane Renmei";
	}
	
	@Autowired
	AnimeService service;

	@Autowired
	AnimeRepository animeRepo;

	@Autowired
	DailyDataRepository dailyRepo;

	@Test
	void saveAndGetTest(){
		List<Staff> stafflist = service.getAnimeStaff(anime2.id);
		StaffList staffObject = new StaffList(anime2, stafflist);

		animeRepo.deleteById(anime2.id);
		
		StaffList fromRepo = animeRepo.findById(anime2.id).orElse(null);

		if(fromRepo == null){
			animeRepo.save(staffObject);
			fromRepo = animeRepo.findById(anime2.id).orElse(null);
		}

		for(Staff a : fromRepo.staffList){
			System.out.println(a.name);
			System.out.println("\t" + a.id);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.positions);
			System.out.println();
		}

		assertFalse(fromRepo == null);
		assertEquals(staffObject, fromRepo);
	}

	@Test
	void saveDailyDataTest(){
		List<Entry> connection = new ArrayList<>();
		connection = List.of(anime1, anime2);

		dailyRepo.save(new DailyData(anime1, anime2, connection));
		DailyData fromRepo = dailyRepo.findById(LocalDate.of(2026, 5, 27)).orElse(null);

		assertFalse(fromRepo == null);

		System.out.println(fromRepo.anime1.name);
		System.out.println(fromRepo.anime2.name);
	}
}
