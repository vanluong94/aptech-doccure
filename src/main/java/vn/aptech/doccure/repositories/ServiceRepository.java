package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.User;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    List<Service> findAllByDoctors(User doctor);
}
