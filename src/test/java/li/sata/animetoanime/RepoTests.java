package li.sata.animetoanime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;

//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

import org.junit.jupiter.api.Test;
//import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.auditing.AuditingHandler;
//import org.springframework.data.auditing.DateTimeProvider;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.repomodels.DailyData;

@SpringBootTest
@ActiveProfiles("dev")
class RepoTests {
	Anime anime1 = new Anime();
	Anime anime2 = new Anime();
	Staff staff = new Staff();

	RepoTests(){
		anime1.entryId = 9874;
		anime2.entryId = 60326;
		anime1.name = "Touhou Niji Sousaku Doujin Anime: Musou Kakyou";
		anime2.name = "Watashi ga Koibito ni Nareru Wake Nai jan, Muri Muri! (※Muri ja Nakatta!?)";
		anime1.imageUrl = "https://cdn.myanimelist.net/images/anime/3/27302l.jpg";
		anime2.imageUrl = "https://cdn.myanimelist.net/images/anime/1887/150496l.jpg";

		staff.name = "hideaki anno";
		staff.entryId = 5111;
	}
	
	@Autowired
	AnimeService service;

	@Autowired
	AnimeRepository animeRepo;

	@Autowired
	StaffRepository staffRepo;

	@Autowired
	DailyDataRepository dailyRepo;

	@Test
	void saveAndGetStaffListTest(){
		// get staff list from api
		List<Staff> staffList = service.getAnimeStaff(anime1.entryId);

		// delete existing references in db
		staffRepo.deleteAllBySourceId(anime1.entryId);
		
		// attempt to get staff list from db (should return null or empty)
		List<Staff> fromRepo = staffRepo.findAllBySourceId(anime1.entryId);

		if(fromRepo == null || fromRepo.isEmpty()){
			// save api data to db
			staffRepo.saveAll(staffList);

			// retirieve from db
			fromRepo = staffRepo.findAllBySourceId(anime1.entryId);
		}

		assertFalse(fromRepo == null || fromRepo.isEmpty()); // prove repo retrieval works

		System.out.println("\nFrom Repo:");
		for(Staff a : fromRepo){
			System.out.println(a.name);
			System.out.println("\t" + a.entryId);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.positions);
			System.out.println();
		}

		System.out.println("\nFrom API:");
		for(Staff a : staffList){
			System.out.println(a.name);
			System.out.println("\t" + a.entryId);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.positions);
			System.out.println();
		}

		// clear for next test
		staffRepo.deleteAllBySourceId(anime1.entryId);
	}

	@Test
	void saveAndGetAnimeListTest(){
		// get anime list from api
		List<Anime> animeList = service.getStaffAnime(staff.entryId);

		// delete existing ref in db
		animeRepo.deleteAllBySourceId(staff.entryId);
		
		// try getting anime list from db (should be empty / null)
		List<Anime> fromRepo = animeRepo.findAllBySourceId(staff.entryId);

		if(fromRepo == null || fromRepo.isEmpty()){
			// save api data to db
			animeRepo.saveAll(animeList);

			// retrieve from db
			fromRepo = animeRepo.findAllBySourceId(staff.entryId);
		}

		// prove repo retrieval works
		assertFalse(fromRepo == null || fromRepo.isEmpty());
		// assertEquals(animeObject.id, fromRepo.id);

		System.out.println("\nFrom Repo:");
		for(Anime a : fromRepo){
			System.out.println(a.name);
			System.out.println("\t" + a.entryId);
			System.out.println("\t" + a.imageUrl);
			System.out.println();
		}

		System.out.println("\nFrom API:");
		for(Anime a : animeList){
			System.out.println(a.name);
			System.out.println("\t" + a.entryId);
			System.out.println("\t" + a.imageUrl);
			System.out.println();
		}

		// clear for next test
		animeRepo.deleteAllBySourceId(staff.entryId);
	}

	@Test
	void saveDailyDataTest(){
		DailyData exampleData = new DailyData();

		List<Entry> connection = new ArrayList<>();
		connection = List.of(anime1, anime2);

		exampleData.name = "example daily data set";
		exampleData.anime1 = anime1;
		exampleData.anime2 = anime2;
		exampleData.shortestPath = connection;

		dailyRepo.save(exampleData);
		DailyData fromRepo = dailyRepo.findById(DailyData.currentWorkingDate()).orElse(null);

		assertFalse(fromRepo == null);

		System.out.println("from repo: ");
		System.out.println(fromRepo);
		System.out.println(fromRepo.name);
		System.out.println(fromRepo.anime1.name);
		System.out.println(fromRepo.anime2.name);
		System.out.print("Path: ");
		for(Entry a : fromRepo.shortestPath)
			System.out.print("-> " + a.name);

		System.out.println("saved at: " + DailyData.currentWorkingDate());
		//dailyRepo.deleteById(LocalDate.from(ZonedDateTime.now(ZoneId.of("UTC+14:00"))));
	}

	@Test
	void dailyDataDeletionTest() {
		DailyData oldest = new DailyData(DailyData.currentWorkingDate().minusDays(2));
		DailyData middlest = new DailyData(DailyData.currentWorkingDate().minusDays(1));
		DailyData newest = new DailyData();

		oldest.name = "oldest";
		middlest.name = "old";
		newest.name = "newset";

		dailyRepo.saveAll(List.of(oldest, middlest, newest));

        dailyRepo.deleteById(DailyData.currentWorkingDate().minusDays(2));

		assertTrue(dailyRepo.findById(oldest.date).orElse(null) == null);
		assertTrue(dailyRepo.findById(middlest.date).orElse(null) != null);
		assertTrue(dailyRepo.findById(newest.date).orElse(null) != null);

		dailyRepo.delete(oldest);
		dailyRepo.delete(middlest);
		dailyRepo.delete(newest);
	}
	
	// idk how to test this properly but i think it works 
//	@MockitoBean
//    DateTimeProvider testDateTimeProvider;
//
//	@Spy
//	private AuditingHandler handler;
//
//	@Test
//	void staffRepoDeletionTest() {
//		handler.setDateTimeProvider(testDateTimeProvider);
//
//		// clearing repo for test
//		staffRepo.deleteById(1);
//		staffRepo.deleteById(2);
//		staffRepo.deleteById(3);
//
//		// creating test objects
//		AnimeList oldest = new AnimeList(1, null);
//		AnimeList middlest = new AnimeList(2, null);
//		AnimeList newest = new AnimeList(3, null);
//
//		// saving with default localdate
//		staffRepo.save(newest);
//
//		// saving one day previous
//		when(testDateTimeProvider.getNow()).thenReturn(Optional.of(LocalDate.now().minusDays(1)));
//		staffRepo.save(middlest);
//
//		// saving two days previous
//		when(testDateTimeProvider.getNow()).thenReturn(Optional.of(LocalDate.now().minusDays(2)));
//		staffRepo.save(oldest);
//
//		// should only delete oldest entry
//		staffRepo.deleteByCreationLessThan(LocalDate.now().minusDays(1));
//
//		System.out.println("Deleting at: ");
//		System.out.println(LocalDate.now().minusDays(1).toString());
//		System.out.println();
//		System.out.println("Found: ");
//		System.out.println(staffRepo.findById(1).orElse(null).creation.toString());
//		System.out.println(staffRepo.findById(2).orElse(null).creation.toString());
//		System.out.println(staffRepo.findById(3).orElse(null).creation.toString());
//		System.out.println();
//
//		// assertions: oldest should be deleted, everything else should be retained
//		assertTrue(staffRepo.findById(1).orElse(null) == null); // oldest
//		assertTrue(staffRepo.findById(3).orElse(null) != null); // newest
//		assertTrue(staffRepo.findById(2).orElse(null) != null); // middlest
//
//		// cleaning test data
//		staffRepo.delete(oldest);
//		staffRepo.delete(middlest);
//		staffRepo.delete(newest);
//	}
}
