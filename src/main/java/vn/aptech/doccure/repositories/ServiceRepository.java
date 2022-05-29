package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.User;

import java.util.List;

import java.util.Optional;

public interface ServiceRepository extends CrudRepository<Service, Long> {

    List<Service> findAllByDoctors(User doctor);

    Optional<Service> findBySlug(String slug);

}
