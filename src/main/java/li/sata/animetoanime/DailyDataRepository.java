package li.sata.animetoanime;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import li.sata.animetoanime.repomodels.DailyData;

public interface DailyDataRepository extends CrudRepository<DailyData, LocalDate>{
}
