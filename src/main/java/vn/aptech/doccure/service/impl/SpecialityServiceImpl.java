package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.repositories.SpecialityRepository;
import vn.aptech.doccure.service.SpecialityService;

import java.util.Optional;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    @Autowired
    private SpecialityRepository repo;

    @Override
    public Iterable<Speciality> findAll() {
        return repo.findAll();
    }

    @Override
    public Iterable<Speciality> findAllByOrderByIdDesc() {
        return repo.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<Speciality> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Speciality save(Speciality speciality) {
        return repo.save(speciality);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
