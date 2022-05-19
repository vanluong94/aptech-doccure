package vn.aptech.doccure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.repository.TimeSlotRepository;
import vn.aptech.doccure.service.TimeSlotService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeSlotService timeSlotService;

    @Scheduled(fixedRate = 60*60*24*1000)
    public void appointmentsGenerator() {

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
                    timeSlotRepository.save(timeSlot);
                    count++;
                }
            }
        }

        logger.info("Inserted " + count + " time slots");
        logger.info("==================== [Time Slots Generator - END] =============================");

    }

}
