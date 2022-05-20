package vn.aptech.doccure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;

import java.util.List;

@Repository
public interface TimeSlotDefaultRepository extends JpaRepository<TimeSlotDefault, Long> {
    List<TimeSlotDefault> findAllByDoctor(User doctor);
}