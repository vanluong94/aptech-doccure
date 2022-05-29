package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.User;

import java.util.List;

public interface ServiceService {
    Iterable<Service> findAll();

    Service save(Service service);

    List<Service> findByDoctor(User doctor);
}
