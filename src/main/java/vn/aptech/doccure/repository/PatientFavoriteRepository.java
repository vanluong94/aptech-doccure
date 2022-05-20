package vn.aptech.doccure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;

@Repository
public interface PatientFavoriteRepository extends JpaRepository<PatientFavorite, PatientFavoriteId> {
}