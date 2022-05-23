package vn.aptech.doccure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findTop10ByOrderByIdDesc();

    List<User> findAllByGenderInAndSpecialitiesIn(List<Short> gender, List<Speciality> specialities);

    List<User> findTop10ByRolesInOrderByIdDesc(Set<Role> roles);

    Long countByRolesIn(Set<Role> roles);

    @Query(value = "SELECT u FROM User u" +
            " LEFT JOIN u.services service" +
            " LEFT JOIN u.specialities speciality" +
            " LEFT JOIN u.clinic c" +
            " LEFT JOIN u.roles r" +
            " WHERE r.name in (:roles)" +
            " AND ((:gender) IS NULL OR u.gender in (:gender))" +
            " AND ((:specialities) IS NULL OR speciality.id in (:specialities))" +
            " AND (:query IS NULL OR (u.firstName LIKE CONCAT('%',:query ,'%') OR u.lastName LIKE CONCAT('%',:query ,'%') OR service.name LIKE CONCAT('%',:query ,'%')))" +
            " AND (:location IS NULL OR (c.name like CONCAT('%',:location ,'%') OR c.city LIKE CONCAT('%',:location ,'%') OR c.country LIKE CONCAT('%',:location ,'%')))" +
            " ORDER BY u.id desc")
    List<User> findAllWithAdvanceSearch(@Param("location") String location,
                                        @Param("query") String query,
                                        @Param("gender") Collection<Short> gender,
                                        @Param("specialities") Collection<Long> specialities,
                                        @Param("roles") Collection<String> roles);
}