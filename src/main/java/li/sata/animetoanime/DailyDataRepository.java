package li.sata.animetoanime;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import li.sata.animetoanime.repomodels.DailyData;

@Repository
public interface DailyDataRepository extends CrudRepository<DailyData, LocalDate>{
}
