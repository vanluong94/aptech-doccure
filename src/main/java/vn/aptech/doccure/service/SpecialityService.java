package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Speciality;

public interface SpecialityService {
    Iterable<Speciality> findAll();

    Speciality save(Speciality speciality);
}
