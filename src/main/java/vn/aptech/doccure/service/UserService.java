package vn.aptech.doccure.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    User save(User user);

    Optional<User> findByEmail(String email);

    Iterable<User> findAll();

    Optional<User> findById(Long id);

    boolean existByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User getCurrentUser();

    List<User> findTop10ByRolesInOrderByIdDesc(Set<Role> roles);

    Long countByRolesIn(Set<Role> roles);

}