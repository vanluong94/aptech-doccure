package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.Speciality;

@Repository
public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
    Iterable<Speciality> findAllByOrderByIdDesc();
}
