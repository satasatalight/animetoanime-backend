package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import li.sata.animetoanime.genericmodels.Anime;
import li.sata.animetoanime.genericmodels.EntryCompositeKey;

public interface AnimeRepository extends CrudRepository<Anime, EntryCompositeKey>{
    @Transactional
    void deleteAllByCreationLessThan(LocalDate a);

    @Transactional 
    void deleteAllBySourceId(int id);

    @Transactional
    List<Anime> findAllBySourceId(int id);
}