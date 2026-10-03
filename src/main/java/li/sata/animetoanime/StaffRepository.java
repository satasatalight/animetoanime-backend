package li.sata.animetoanime;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import li.sata.animetoanime.genericmodels.EntryCompositeKey;
import li.sata.animetoanime.genericmodels.Staff;

public interface StaffRepository extends CrudRepository<Staff, EntryCompositeKey>{
    @Transactional
    void deleteAllByCreationLessThan(LocalDate a);

    @Transactional 
    void deleteAllBySourceId(int id);

    @Transactional
    List<Staff> findAllBySourceId(int id);
}