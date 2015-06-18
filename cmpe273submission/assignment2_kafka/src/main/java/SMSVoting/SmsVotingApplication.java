package SMSVoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmsVotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(config.class, args);
    }
}
