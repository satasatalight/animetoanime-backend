package li.sata.animetoanime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import li.sata.animetoanime.repomodels.StaffList;

@Repository
public interface AnimeRepository extends CrudRepository<StaffList, Integer>{
}