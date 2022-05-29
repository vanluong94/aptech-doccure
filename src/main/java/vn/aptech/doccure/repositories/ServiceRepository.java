package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Service;

import java.util.Optional;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    Optional<Service> findBySlug(String slug);
}
