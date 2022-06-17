package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.TimeSlotDefault;
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
    public List<TimeSlot> saveAll(Iterable<TimeSlot> timeSlots) {
        return timeSlotRepository.saveAll(timeSlots);
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        timeSlotRepository.delete(timeSlot);
    }

    @Override
    public void deleteAll(Iterable<TimeSlot> timeSlots) {
        timeSlotRepository.deleteAll(timeSlots);
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
                "  ,slot_default.status AS status " +
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
    public List<TimeSlot> findAllByDoctorOnDate(User doctor, LocalDateTime date) {
        String sql = "" +
                "SELECT" +
                "  slots.id" +
                "  ,slots.appointment_id " +
                "  ,slot_default.doctor_id " +
                "  ,slot_default.slot_datetime_start AS time_start " +
                "  ,slot_default.slot_datetime_end AS time_end " +
                "  ,slot_default.status AS status " +
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
                "      AND doctor_id = :doctorId" +
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
                .setParameter("theDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .setParameter("doctorId", doctor.getId());

        return query.getResultList();
    }

    @Override
    public List<TimeSlot> findAvailableTimeSlotByDoctorOnDate(Long id, LocalDateTime date) {
        return timeSlotRepository.findAvailableTimeSlotByDoctorOnDate(id, date);
    }

    @Override
    public List<TimeSlot> findExistedTimeSlotByTimeSlotDefault(TimeSlotDefault timeSlotDefault) {

        String sql = "" +
                "SELECT" +
                "  slots.id" +
                "  ,slots.appointment_id " +
                "  ,slots.doctor_id " +
                "  ,slots.time_start AS time_start " +
                "  ,slots.time_end AS time_end " +
                "  ,slots_default.status AS status " +
                "FROM time_slots_default as slots_default " +
                "JOIN time_slots AS slots " +
                "ON " +
                "   slots.doctor_id = slots_default.doctor_id " +
                "   AND WEEKDAY(slots.time_start) = slots_default.weekday " +
                "   AND DATE_FORMAT(slots.time_start, '%H:%i:%s') = slots_default.time_start " +
                "   AND DATE_FORMAT(slots.time_end, '%H:%i:%s') = slots_default.time_end " +
                "WHERE " +
                "   slots.doctor_id = :doctorId " +
                "   AND slots_default.weekday = :weekday " +
                "   AND slots_default.time_start = :timeStart" +
                "   AND slots_default.time_end = :timeEnd ";
        Query query = entityManager.createNativeQuery(sql,"TimeSlotDTOMapping")
                .setParameter("timeStart", timeSlotDefault.getTimeStart().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .setParameter("timeEnd", timeSlotDefault.getTimeEnd().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .setParameter("weekday", timeSlotDefault.getWeekday())
                .setParameter("doctorId", timeSlotDefault.getDoctorId());

        return query.getResultList();
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
