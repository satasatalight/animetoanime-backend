package li.sata.animetoanime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import li.sata.animetoanime.repomodels.AnimeList;

@Repository
public interface StaffRepository extends CrudRepository<AnimeList, Integer>{
}