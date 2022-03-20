package vn.aptech.doccure.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.aptech.doccure.entities.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User save(User user);

    Optional<User> findByEmail(String email);

    Iterable<User> findAll();

    Optional<User> findById(Long id);

    boolean existByEmail(String email);
}