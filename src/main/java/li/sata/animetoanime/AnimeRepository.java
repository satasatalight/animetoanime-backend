package li.sata.animetoanime;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import li.sata.animetoanime.genericmodels.Staff;

@Repository
public interface AnimeRepository extends ListCrudRepository<List<Staff>, Integer>{
    List<Staff> findById(int id);
}