package vn.aptech.doccure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {
}
