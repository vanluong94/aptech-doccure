package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;
import vn.aptech.doccure.entities.User;

import java.util.Optional;

public interface PatientFavoriteService {

    PatientFavorite saveAndFlush(PatientFavorite favorite);

    Optional<PatientFavorite> findById(PatientFavoriteId id);

    void delete(PatientFavorite favorite);

    boolean isDoctorFavorited(User doctor, User patient);

}
