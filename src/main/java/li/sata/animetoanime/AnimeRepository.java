package li.sata.animetoanime;

import org.springframework.data.repository.CrudRepository;

import li.sata.animetoanime.repomodels.StaffList;

public interface AnimeRepository extends CrudRepository<StaffList, Integer>{
}