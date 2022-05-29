package vn.aptech.doccure.service;

import org.springframework.data.domain.Sort;
import vn.aptech.doccure.entities.Speciality;

import java.util.Optional;

public interface SpecialityService {
    Iterable<Speciality> findAll();

    Iterable<Speciality> findAllByOrderByIdDesc();

    Optional<Speciality> findById(Long id);

    Optional<Speciality> findBySlug(String slug);

    Speciality save(Speciality speciality);

    void deleteById(Long id);
}
