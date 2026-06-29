package li.sata.animetoanime;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.Entry;

@SpringBootTest
class AnimeToAnimeServiceTests {
	Anime anime1 = new Anime();
	Anime anime2 = new Anime();

	AnimeToAnimeServiceTests(){
		anime1.id = 387;
		anime2.id = 11809;
		anime1.name = "Haibane Renmei";
		anime2.name = "gdgd Fairies";
	}

	@Autowired
	AnimeToAnimeService service;

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
