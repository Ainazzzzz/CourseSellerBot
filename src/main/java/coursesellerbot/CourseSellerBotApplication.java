package coursesellerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CourseSellerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseSellerBotApplication.class, args);
    }

}
