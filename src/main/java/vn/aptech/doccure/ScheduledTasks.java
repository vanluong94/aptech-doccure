package vn.aptech.doccure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.AppointmentLog;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.TimeSlotService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private AppointmentService appointmentService;

    @Scheduled(fixedRate = 60*60*24*1000)
    public void timeSlotsGenerator() {

        LocalDateTime now = LocalDateTime.now().plusDays(1);

        logger.info("==================== [Time Slots Generator - BEGIN] =============================");
        logger.info("Task: Generate Time Slots for the next 2 weeks");
        logger.info("Timestamp: [" + now.toString() + "]");

        int count = 0;
        for (int i=0; i<14; i++) {
            LocalDateTime theDate = now.plusDays(i);
            List<TimeSlot> timeSlots = timeSlotService.findAllByDate(theDate);

            for (TimeSlot timeSlot : timeSlots) {
                if (!timeSlot.exists()) {
                    timeSlotService.saveAndFlush(timeSlot);
                    count++;
                }
            }
        }

        logger.info("Inserted " + count + " time slots");
        logger.info("==================== [Time Slots Generator - END] =============================");

    }

    @Scheduled(fixedRate = 60*60*1000)
    @Transactional
    public void appointmentsAutoComplete() {

        List<Appointment> appointments = appointmentService.findAllPastIncomplete();

        for (Appointment apmt : appointments) {

            AppointmentLog log = new AppointmentLog();
            log.setAppointment(apmt);

            switch (apmt.getStatus()) {
                case Appointment.STATUS.PENDING:
                    apmt.setStatus(Appointment.STATUS.CANCELED);
                    apmt.getTimeSlot().setAppointment(null);
                    log.setContent("Canceled by system due to overdue");
                    apmt.getLogs().add(log);
                    break;
                case Appointment.STATUS.CONFIRMED:
                    apmt.setStatus(Appointment.STATUS.COMPLETED);
                    log.setContent("Completed by system");
                    apmt.getLogs().add(log);
                    break;
            }
        }

        appointmentService.saveAllAndFlush(appointments);
    }
}
