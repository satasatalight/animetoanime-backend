package li.sata.animetoanime;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import li.sata.animetoanime.repomodels.AnimeList;

public interface StaffRepository extends CrudRepository<AnimeList, Integer>{
    @Transactional
    void deleteByCreationLessThan(LocalDate a);
}