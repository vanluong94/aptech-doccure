package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
}
