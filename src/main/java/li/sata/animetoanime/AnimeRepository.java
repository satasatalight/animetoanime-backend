package li.sata.animetoanime;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import li.sata.animetoanime.repomodels.StaffList;

public interface AnimeRepository extends CrudRepository<StaffList, Integer>{
    @Transactional
    void deleteByCreationLessThan(LocalDate a);
}