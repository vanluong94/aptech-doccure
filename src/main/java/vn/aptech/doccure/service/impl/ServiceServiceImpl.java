package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.ServiceRepository;
import vn.aptech.doccure.service.ServiceService;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository repo;
    @Override
    public Iterable<Service> findAll() {
        return repo.findAll();
    }
    @Override
    public Service save(Service service) {
        return repo.save(service);
    }
    @Override
    public Optional<Service> findById(Long Id) {return repo.findById(Id);}
    @Override
    public Optional<Service> findBySlug(String slug) {
        return repo.findBySlug(slug);
    }

    @Override
    public void deleteById(Long Id) { repo.deleteById(Id); }

    @Override
    public List<Service> findByDoctor(User doctor) {
        return repo.findAllByDoctors(doctor);
    }
}
