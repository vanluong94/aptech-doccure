package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    List<Appointment> findAllByDoctorAndDate(Long doctorId, LocalDateTime date);

    List<Appointment> findAllByDate(LocalDateTime date);

    List<Appointment> findAllByDoctorOnDate(Long doctorId, LocalDateTime date);
}
