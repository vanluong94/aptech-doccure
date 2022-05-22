package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.PatientFavoriteRepository;
import vn.aptech.doccure.service.PatientFavoriteService;

import java.util.Optional;

@Service
public class PatientFavoriteServiceImpl implements PatientFavoriteService {

    @Autowired
    private PatientFavoriteRepository favoriteRepository;

    @Override
    public PatientFavorite saveAndFlush(PatientFavorite favorite) {
        return favoriteRepository.saveAndFlush(favorite);
    }

    @Override
    public Optional<PatientFavorite> findById(PatientFavoriteId id) {
        return favoriteRepository.findById(id);
    }

    @Override
    public void delete(PatientFavorite favorite) {
        favoriteRepository.delete(favorite);
    }

    @Override
    public boolean isDoctorFavorited(User doctor, User patient) {
        PatientFavoriteId favoriteId = new PatientFavoriteId(doctor, patient);
        return this.findById(favoriteId).isPresent();
    }

    @Override
    public Page<PatientFavorite> findByPatient(User patient, Pageable pageable) {
        return favoriteRepository.findByIdPatientOrderByCreatedAtDesc(patient, pageable);
    }
}
