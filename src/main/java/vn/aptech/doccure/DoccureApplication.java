package vn.aptech.doccure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import vn.aptech.doccure.storage.StorageProperties;
import vn.aptech.doccure.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class}) // exclude spring white label page
public class DoccureApplication {

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



}
