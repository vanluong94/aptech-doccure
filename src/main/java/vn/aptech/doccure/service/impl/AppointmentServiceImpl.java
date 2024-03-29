package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.AppointmentRepository;
import vn.aptech.doccure.service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Long count() {
        return appointmentRepository.count();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment saveAndFlush(Appointment appointment) {
        return appointmentRepository.saveAndFlush(appointment);
    }

    @Override
    public List<Appointment> saveAllAndFlush(List<Appointment> appointments) {
        return appointmentRepository.saveAllAndFlush(appointments);
    }

    @Override
    public List<Appointment> findAllPastIncomplete() {
        return appointmentRepository.findAllPastIncomplete();
    }

    @Override
    public List<Appointment> findAvailableByDoctorTimeRange(User doctor, LocalDateTime startingDate, LocalDateTime endingDate) {
        return appointmentRepository.findAllByDoctorAndStatusInAndTimeSlotTimeStartBetween(
                doctor,
                Arrays.asList(
                        Appointment.STATUS.CONFIRMED,
                        Appointment.STATUS.PENDING
                ),
                startingDate,
                endingDate
        );
    }

    @Override
    public Page<Appointment> findAllByPatient(User patient, Pageable pageable) {
        return appointmentRepository.findAllByPatientOrderByCreatedDateDesc(patient, pageable);
    }

    @Override
    public Page<Appointment> findAllByDoctor(User doctor, Pageable pageable) {
        return appointmentRepository.findAllByDoctorOrderByCreatedDateDesc(doctor, pageable);
    }

    @Override
    public Page<Appointment> findUpcomingByDoctor(User doctor, Pageable pageable) {
        return appointmentRepository.findAllByDoctorAndTimeSlotTimeStartAfterOrderByTimeSlotTimeStartAsc(doctor, LocalDateTime.now(), pageable);
    }

    @Override
    public Page<Appointment> findUpcomingByPatient(User patient, Pageable pageable) {
        return appointmentRepository.findAllByPatientAndTimeSlotTimeStartAfterOrderByTimeSlotTimeStartAsc(patient, LocalDateTime.now(), pageable);
    }

    @Override
    public Page<Appointment> findTodayByDoctor(User doctor, Pageable pageable) {
        return appointmentRepository.findAllByDoctorAndTimeSlotTimeStartBetweenOrderByTimeSlotTimeStartAsc(
                doctor,
                LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(0, 0)
                ),
                LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(0, 0)
                ),
                pageable
        );
    }

    @Override
    public Page<Appointment> findTodayByPatient(User patient, Pageable pageable) {
        return appointmentRepository.findAllByPatientAndTimeSlotTimeStartBetweenOrderByTimeSlotTimeStartAsc(
                patient,
                LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(0, 0)
                ),
                LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(0, 0)
                ),
                pageable
        );
    }

    @Override
    public Long countByDoctor(User doctor) {
        return appointmentRepository.countByDoctor(doctor);
    }

    @Override
    public Long countTodayByDoctor(User doctor) {
        return appointmentRepository.countByDoctorAndTimeSlotTimeStartBetween(
                doctor,
                LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(0, 0)
                ),
                LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(0, 0)
                )
        );
    }

    @Override
    public Long countPatientByDoctor(User doctor) {
        return appointmentRepository.countPatientByDoctor(doctor);
    }

    @Override
    public Page<User> findPatientsByDoctor(User doctor, Pageable pageable) {
        return appointmentRepository.findPatientByDoctor(doctor, pageable);
    }

    @Override
    public Page<Appointment> findByDoctorAndPatient(User doctor, User patient, Pageable pageable) {
        return appointmentRepository.findAllByDoctorAndPatient(doctor, patient, pageable);
    }

    @Override
    public List<Appointment> findTop10Latest() {
        return appointmentRepository.findTop5ByOrderByCreatedDateDesc();
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }
}
