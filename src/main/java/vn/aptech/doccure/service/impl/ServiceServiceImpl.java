package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.repository.ServiceRepository;
import vn.aptech.doccure.service.ServiceService;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Iterable<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Service save(Service service) {
        return serviceRepository.save(service);
    }
}
