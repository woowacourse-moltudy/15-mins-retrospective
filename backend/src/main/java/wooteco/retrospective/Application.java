package wooteco.retrospective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
    Logger logger = LoggerFactory.getLogger(Application.class);

    @PostConstruct
    public void timezoneSetup() {
        TimeZone serverDefaultTimeZone = TimeZone.getDefault();
        TimeZone seoulTimeZone = TimeZone.getTimeZone("Asia/Seoul");
        if (!serverDefaultTimeZone.hasSameRules(seoulTimeZone)) {
            TimeZone.setDefault(seoulTimeZone);
            logger.info(
                String.format("TimeZone Setting Changed : %s -> %s",
                              serverDefaultTimeZone.getDisplayName(),
                              seoulTimeZone.getDisplayName()
                )
            );
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
