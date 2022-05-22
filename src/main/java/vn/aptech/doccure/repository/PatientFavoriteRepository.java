package vn.aptech.doccure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;
import vn.aptech.doccure.entities.User;

public interface PatientFavoriteRepository extends JpaRepository<PatientFavorite, PatientFavoriteId> {

    Page<PatientFavorite> findByIdPatientOrderByCreatedAtDesc(User patient, Pageable pageable);

}