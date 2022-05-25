package vn.aptech.doccure.utils;

import vn.aptech.doccure.entities.Appointment;

import java.util.*;

public class AppointmentUtils {

    public static List<Object> toDataTable(List<Appointment>appointments) {

        List<Object> rows = new LinkedList<>();

        for (Appointment apmt : appointments) {
            Map<String, Object> row = new LinkedHashMap<>();
            Map<String, Object> doctor = new HashMap<>();
            Map<String, Object> patient = new HashMap<>();

            doctor.put("avatar", apmt.getDoctor().getTheAvatar());
            doctor.put("url", DoctorUtils.getDoctorProfileUrl(apmt.getDoctor()));
            doctor.put("title", apmt.getDoctor().getDoctorTitle());
            doctor.put("subtitle", apmt.getDoctorDTO().getSpecialtiesText());

            patient.put("avatar", apmt.getPatient().getTheAvatar());
            patient.put("url", "#");
            patient.put("title", apmt.getPatient().getFullName());
            patient.put("subtitle", "#" + apmt.getPatient().getId());

            row.put("id", apmt.getId());
            row.put("doctor", doctor);
            row.put("patient", patient);
            row.put("apmtDate", DateUtils.toStandardDate(apmt.getTimeSlot().getTimeStart()));
            row.put("timeStart", DateUtils.toStandardTime(apmt.getTimeSlot().getTimeStart()));
            row.put("timeEnd", DateUtils.toStandardTime(apmt.getTimeSlot().getTimeEnd()));
            row.put("bookingDate", DateUtils.toStandardDate(apmt.getCreatedDate()));
            row.put("status", apmt.getStatus());
            row.put("action", "");

            rows.add(row);
        }

        return rows;

    }
}
