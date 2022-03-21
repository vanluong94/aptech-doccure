package vn.aptech.doccure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}