package li.sata.animetoanime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;
import li.sata.animetoanime.genericmodels.Staff;
import li.sata.animetoanime.repomodels.AnimeList;
import li.sata.animetoanime.repomodels.DailyData;
import li.sata.animetoanime.repomodels.StaffList;

@SpringBootTest
class RepoTests {
	Anime anime1 = new Anime();
	Anime anime2 = new Anime();
	Staff staff = new Staff();

	RepoTests(){
		anime1.id = 9874;
		anime2.id = 60326;
		anime1.name = "Touhou Niji Sousaku Doujin Anime: Musou Kakyou";
		anime2.name = "Watashi ga Koibito ni Nareru Wake Nai jan, Muri Muri! (※Muri ja Nakatta!?)";
		anime1.imageUrl = "https://cdn.myanimelist.net/images/anime/3/27302l.jpg";
		anime2.imageUrl = "https://cdn.myanimelist.net/images/anime/1887/150496l.jpg";

		staff.name = "hideaki anno";
		staff.id = 5111;
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
	void saveAndGetAnimeListTest(){
		List<Staff> stafflist = service.getAnimeStaff(anime1.id);
		StaffList staffObject = new StaffList(anime1, stafflist);

		animeRepo.deleteById(anime1.id);
		
		StaffList fromRepo = animeRepo.findById(anime1.id).orElse(null);

		if(fromRepo == null){
			animeRepo.save(staffObject);
			fromRepo = animeRepo.findById(anime1.id).orElse(null);
		}

		assertFalse(fromRepo == null);
		assertEquals(staffObject.id, fromRepo.id);

		System.out.println("\nFrom Repo:");
		for(Staff a : fromRepo.staffList){
			System.out.println(a.name);
			System.out.println("\t" + a.id);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.positions);
			System.out.println();
		}

		System.out.println("\nFrom Staff Object:");
		for(Staff a : staffObject.staffList){
			System.out.println(a.name);
			System.out.println("\t" + a.id);
			System.out.println("\t" + a.imageUrl);
			System.out.println("\t" + a.positions);
			System.out.println();
		}

		animeRepo.deleteById(anime1.id);
	}

	@Test
	void saveAndGetStaffListTest(){
		List<Anime> animeList = service.getStaffAnime(staff.id);
		AnimeList animeObject = new AnimeList(staff, animeList);

		staffRepo.deleteById(staff.id);
		
		AnimeList fromRepo = staffRepo.findById(staff.id).orElse(null);

		if(fromRepo == null){
			staffRepo.save(animeObject);
			fromRepo = staffRepo.findById(staff.id).orElse(null);
		}

		assertFalse(fromRepo == null);
		assertEquals(animeObject.id, fromRepo.id);

		System.out.println("\nFrom Repo:");
		for(Anime a : fromRepo.animeList){
			System.out.println(a.name);
			System.out.println("\t" + a.id);
			System.out.println("\t" + a.imageUrl);
			System.out.println();
		}

		System.out.println("\nFrom Staff Object:");
		for(Anime a : animeObject.animeList){
			System.out.println(a.name);
			System.out.println("\t" + a.id);
			System.out.println("\t" + a.imageUrl);
			System.out.println();
		}

		staffRepo.deleteById(staff.id);
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
}
