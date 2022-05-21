package vn.aptech.doccure.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Optional<Appointment> findById(Long id);

    Appointment saveAndFlush(Appointment appointment);

    List<Appointment> saveAllAndFlush(List<Appointment> appointments);

    List<Appointment> findAllPastIncomplete();

    Page<Appointment> findAllByPatient(User patient, Pageable pageable);

    Page<Appointment> findAllByDoctor(User doctor, Pageable pageable);

    Page<Appointment> findUpcomingByDoctor(User doctor, Pageable pageable);

    Page<Appointment> findUpcomingByPatient(User patient, Pageable pageable);

    Page<Appointment> findTodayByDoctor(User doctor, Pageable pageable);

    Page<Appointment> findTodayByPatient(User patient, Pageable pageable);

    Long countByDoctor(User doctor);

    Long countTodayByDoctor(User doctor);

    Long countPatientByDoctor(User doctor);

}
