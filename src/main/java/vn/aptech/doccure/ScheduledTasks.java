package vn.aptech.doccure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Scheduled(fixedRate = 60*60*24*1000)
    public void appointmentsGenerator() {

        LocalDateTime now = LocalDateTime.now().plusDays(1);

        logger.info("==================== [Appointments Generator - BEGIN] =============================");
        logger.info("Task: Generate Appointments for the next 2 weeks");
        logger.info("Timestamp: [" + now.toString() + "]");

        int count = 0;
        for (int i=0; i<14; i++) {
            LocalDateTime theDate = now.plusDays(i);
            List<Appointment> appointments = appointmentService.findAllByDate(theDate);

            for (Appointment apmt : appointments) {
                if (!apmt.exists()) {
                    appointmentRepository.save(apmt);
                    count++;
                }
            }
        }

        logger.info("Inserted " + count + " appointment records");
        logger.info("==================== [Appointments Generator - END] =============================");

    }

}
