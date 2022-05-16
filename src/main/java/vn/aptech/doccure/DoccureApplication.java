package vn.aptech.doccure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.storage.StorageProperties;
import vn.aptech.doccure.storage.StorageService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class}) // exclude spring white label page
@EnableScheduling
public class DoccureApplication {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public static void main(String[] args) {
        SpringApplication.run(DoccureApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            storageService.deleteAll(); // Xóa toàn bộ resources upload
            storageService.init();
        };
    }

    @Scheduled(fixedRate = 60*60*24*1000)
    public void appointmentsGenerator() {

        LocalDateTime now = LocalDateTime.now().plusDays(1);

        System.out.println("==================== [Appointments Generator - BEGIN] =============================");
        System.out.println("Task: Generate Appointments for the next 2 weeks");
        System.out.println("Timestamp: [" + now.toString() + "]");

        for (int i=0; i<14; i++) {
            LocalDateTime theDate = now.plusDays(i);
            List<Appointment> appointments = appointmentService.findAllByDate(theDate);

            for (Appointment apmt : appointments) {
                System.out.println("[Appointment Data] " + apmt.toString());
                if (!apmt.exists()) {
                    appointmentRepository.save(apmt);
                }
            }
        }

        System.out.println("==================== [Appointments Generator - END] =============================");

    }

}
