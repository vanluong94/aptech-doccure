package vn.aptech.doccure.repository;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}