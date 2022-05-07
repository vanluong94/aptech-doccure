package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Service;

public interface ServiceService {
    Iterable<Service> findAll();

    Service save(Service service);
}
