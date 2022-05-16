package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.service.AppointmentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> findAllByDoctorAndDate(Long doctorId, LocalDateTime date) {

        String sql = "" +
                "SELECT" +
                "  apmts.id" +
                "  ,dts.doctor_id " +
                "  ,apmts.patient_id " +
                "  ,dts.slot_datetime_start AS time_start " +
                "  ,dts.slot_datetime_end AS time_end " +
                "  ,dts.status " +
                "FROM" +
                "  (" +
                "    SELECT" +
                "      *" +
                "      ,CAST(CONCAT(:theDate, ' ', time_start) as DateTime) as slot_datetime_start " +
                "      ,CAST(CONCAT(:theDate, ' ', time_end) as DateTime) as slot_datetime_end " +
                "    FROM" +
                "      appointments_default" +
                "    WHERE" +
                "      doctor_id = :doctorId" +
                "      AND WEEKDAY(:theDate) = weekday" +
                "      AND status = 1" +
                "  ) AS dts" +
                "  LEFT JOIN appointments AS apmts on (" +
                "    apmts.doctor_id = dts.doctor_id" +
                "    AND (" +
                "      (" +
                "        apmts.time_start >= slot_datetime_start" +
                "        AND apmts.time_start <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        apmts.time_end >= slot_datetime_start" +
                "        AND apmts.time_end <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        apmts.time_start <= slot_datetime_start" +
                "        AND apmts.time_end >= slot_datetime_end" +
                "      )" +
                "    )" +
                "  )";
        Query query = entityManager.createNativeQuery(sql,"AppointmentDTOMapping")
                .setParameter("theDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .setParameter("doctorId", doctorId);

        return query.getResultList();
    }

    @Override
    public List<Appointment> findAllByDate(LocalDateTime date) {
        String sql = "" +
                "SELECT" +
                "  apmts.id" +
                "  ,dts.doctor_id " +
                "  ,apmts.patient_id " +
                "  ,dts.slot_datetime_start AS time_start " +
                "  ,dts.slot_datetime_end AS time_end " +
                "  ,apmts.status " +
                "FROM" +
                "  (" +
                "    SELECT" +
                "      *" +
                "      ,CAST(CONCAT(:theDate, ' ', time_start) as DateTime) as slot_datetime_start " +
                "      ,CAST(CONCAT(:theDate, ' ', time_end) as DateTime) as slot_datetime_end " +
                "    FROM" +
                "      appointments_default" +
                "    WHERE" +
                "      WEEKDAY(:theDate) = weekday" +
                "      AND status = 1" +
                "  ) AS dts" +
                "  LEFT JOIN appointments AS apmts on (" +
                "    apmts.doctor_id = dts.doctor_id" +
                "    AND (" +
                "      (" +
                "        apmts.time_start >= slot_datetime_start" +
                "        AND apmts.time_start <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        apmts.time_end >= slot_datetime_start" +
                "        AND apmts.time_end <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        apmts.time_start <= slot_datetime_start" +
                "        AND apmts.time_end >= slot_datetime_end" +
                "      )" +
                "    )" +
                "  )";
        Query query = entityManager.createNativeQuery(sql,"AppointmentDTOMapping")
                .setParameter("theDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return query.getResultList();
    }

    @Override
    public List<Appointment> findAllByDoctorOnDate(Long doctorId, LocalDateTime date) {
        return appointmentRepository.findAllByDoctorOnDate(doctorId, date);
    }

}
