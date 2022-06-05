package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Service;

import java.util.Optional;

public interface ServiceService {
    Iterable<Service> findAll();

    Service save(Service service);

    Optional<Service> findById(Long Id);

    Optional<Service> findBySlug(String slug);

    void deleteById(Long Id);
}
