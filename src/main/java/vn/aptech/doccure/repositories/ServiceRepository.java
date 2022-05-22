package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {
}
