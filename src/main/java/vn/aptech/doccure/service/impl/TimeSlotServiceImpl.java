package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.ClinicOpeningTimes;
import vn.aptech.doccure.repositories.TimeSlotRepository;
import vn.aptech.doccure.service.TimeSlotService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public Optional<TimeSlot> findById(Long id) {
        return timeSlotRepository.findById(id);
    }

    @Override
    public TimeSlot saveAndFlush(TimeSlot timeSlot) {
        return timeSlotRepository.saveAndFlush(timeSlot);
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public List<TimeSlot> findAllByDate(LocalDateTime date) {
        String sql = "" +
                "SELECT" +
                "  slots.id" +
                "  ,slots.appointment_id " +
                "  ,slot_default.doctor_id " +
                "  ,slot_default.slot_datetime_start AS time_start " +
                "  ,slot_default.slot_datetime_end AS time_end " +
                "FROM" +
                "  (" +
                "    SELECT" +
                "      *" +
                "      ,CAST(CONCAT(:theDate, ' ', time_start) as DateTime) as slot_datetime_start " +
                "      ,CAST(CONCAT(:theDate, ' ', time_end) as DateTime) as slot_datetime_end " +
                "    FROM" +
                "      time_slots_default" +
                "    WHERE" +
                "      WEEKDAY(:theDate) = weekday" +
                "      AND status = 1" +
                "  ) AS slot_default" +
                "  LEFT JOIN time_slots AS slots on (" +
                "    slots.doctor_id = slot_default.doctor_id" +
                "    AND (" +
                "      (" +
                "        slots.time_start >= slot_datetime_start" +
                "        AND slots.time_start <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        slots.time_end >= slot_datetime_start" +
                "        AND slots.time_end <= slot_datetime_end" +
                "      )" +
                "      OR (" +
                "        slots.time_start <= slot_datetime_start" +
                "        AND slots.time_end >= slot_datetime_end" +
                "      )" +
                "    )" +
                "  )";
        Query query = entityManager.createNativeQuery(sql,"TimeSlotDTOMapping")
                .setParameter("theDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return query.getResultList();
    }

    @Override
    public List<TimeSlot> findAllByDoctorOnDate(Long id, LocalDateTime date) {
        return timeSlotRepository.findAllByDoctorOnDate(id, date);
    }

    @Override
    public TimeSlot findUpcomingAvailable(User doctor) {
        return timeSlotRepository.findFirstByDoctorAndTimeStartAfterAndAppointmentIsNullOrderByTimeStartAsc(doctor, LocalDateTime.now());
    }

    @Override
    public ClinicOpeningTimes getOpeningTimesOnDate(User doctor, LocalDateTime date) {
        String sql = "SELECT min(time_start) as opening, max(time_end) as closing FROM time_slots WHERE doctor_id = :doctorId AND CAST(time_start AS DATE) = CAST(:theDate AS DATE)";
        Query query = entityManager.createNativeQuery(sql,"ClinicOpeningTimesMapping")
                .setParameter("doctorId", doctor.getId())
                .setParameter("theDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return (ClinicOpeningTimes) query.getSingleResult();
    }

    @Override
    public List<ClinicOpeningTimes> getAllOpeningTimes(User doctor) {

        List<ClinicOpeningTimes> results = new LinkedList<>();

        LocalDateTime today = LocalDateTime.now();
        int todayDayOfWeek = today.getDayOfWeek().getValue();

        for (int i=1; i<8; i++) {
            LocalDateTime thatDay = today.plusDays(i - todayDayOfWeek);
            results.add(this.getOpeningTimesOnDate(doctor, thatDay));
        }

        return results;
    }
}
