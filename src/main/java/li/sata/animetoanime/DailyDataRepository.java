package li.sata.animetoanime;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import li.sata.animetoanime.repomodels.DailyData;

public interface DailyDataRepository extends CrudRepository<DailyData, LocalDate>{
    @Transactional
    void deleteByDateLessThan(LocalDate date);
}
