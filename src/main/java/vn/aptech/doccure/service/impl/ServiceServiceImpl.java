package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.repositories.ServiceRepository;
import vn.aptech.doccure.service.ServiceService;

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
    public Optional<Service> findBySlug(String slug) {
        return repo.findBySlug(slug);
    }
}
