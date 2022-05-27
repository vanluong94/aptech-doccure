package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Speciality;

import java.util.Optional;

public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
    Iterable<Speciality> findAllByOrderByIdDesc();

    Optional<Speciality> findBySlug(String slug);
}
