package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.repository.SpecialityRepository;
import vn.aptech.doccure.service.SpecialityService;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;

    @Override
    public Iterable<Speciality> findAll() {
        return specialityRepository.findAll();
    }

    @Override
    public Speciality save(Speciality speciality) {
        return specialityRepository.save(speciality);
    }
}
