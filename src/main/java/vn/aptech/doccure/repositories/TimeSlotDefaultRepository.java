package vn.aptech.doccure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;

import java.util.List;

public interface TimeSlotDefaultRepository extends JpaRepository<TimeSlotDefault, Long> {
    
    List<TimeSlotDefault> findAllByDoctor(User doctor);

}