package vn.aptech.doccure.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.UserRepository;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.utils.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repo.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        User theUser = user.get();
        theUser.setAuthorities(getAuthorities(theUser));
        return theUser;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    @Override
    public User save(User user) {
        return repo.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public Iterable<User> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public List<User> findTop10ByOrderByIdDesc() {
        return repo.findTop10ByOrderByIdDesc();
    }

    @Override
    public List<User> findTop10ByRolesInOrderByIdDesc(Set<Role> roles) {
        return repo.findTop10ByRolesInOrderByIdDesc(roles);
    }

    @Override
    public List<User> findAllByRolesInOrderByIdAsc(Set<Role> roles) {
        return repo.findAllByRolesInOrderByIdAsc(roles);
    }

    @Override
    public Long countByRolesIn(Set<Role> roles) {
        return repo.countByRolesIn(roles);
    }

    @Override
    public List<User> findAllByGenderInAndSpecialitiesIn(List<Short> gender, List<Speciality> specialities) {
        return repo.findAllByGenderInAndSpecialitiesIn(gender, specialities);
    }

    public List<User> findAllWithAdvanceSearch(String city, String state, String country, String query, Collection<Short> gender, Collection<Long> specialities, Collection<Long> services, Collection<String> roles) {

        if (StringUtils.isNullOrBlank(city)) {
            city = null;
        }

        if (StringUtils.isNullOrBlank(state)) {
            state = null;
        }

        if (StringUtils.isNullOrBlank(country)) {
            country = null;
        }

        if (StringUtils.isNullOrBlank(query)) {
            query = null;
        }

        return repo.findAllWithAdvanceSearch(city, state, country, query, gender, specialities, services, roles);
    }

    @Override
    public List<User> findAllBySpecialitySlug(String slug, Collection<String> roles) {
        return repo.findAllBySpecialitySlug(slug, roles);
    }

    @Override
    public List<User> findAllByServiceSlug(String slug, Collection<String> roles) {
        return repo.findAllByServiceSlug(slug, roles);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}