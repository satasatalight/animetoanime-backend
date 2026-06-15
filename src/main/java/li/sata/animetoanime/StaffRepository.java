package li.sata.animetoanime;

import org.springframework.data.repository.CrudRepository;

import li.sata.animetoanime.repomodels.AnimeList;

public interface StaffRepository extends CrudRepository<AnimeList, Integer>{
}