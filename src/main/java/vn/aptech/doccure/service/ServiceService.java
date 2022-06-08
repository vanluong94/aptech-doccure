package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.User;

import java.util.List;

import java.util.Optional;

public interface ServiceService {
    Iterable<Service> findAll();

    Service save(Service service);

    Optional<Service> findById(Long Id);

    List<Service> findByDoctor(User doctor);

    Optional<Service> findBySlug(String slug);

    void deleteById(Long Id);
}
