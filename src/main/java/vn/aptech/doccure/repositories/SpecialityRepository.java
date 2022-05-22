package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Speciality;

public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
    Iterable<Speciality> findAllByOrderByIdDesc();
}
