package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Speciality;

import java.util.Optional;

public interface SpecialityService {
    Iterable<Speciality> findAll();

    Optional<Speciality> findById(Long id);

    Speciality save(Speciality speciality);

    void deleteById(Long id);
}
