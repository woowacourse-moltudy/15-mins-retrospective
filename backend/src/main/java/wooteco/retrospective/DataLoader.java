package wooteco.retrospective;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;

import java.time.LocalTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final ConferenceTimeRepository conferenceTimeRepository;

    public DataLoader(ConferenceTimeRepository conferenceTimeRepository) {
        this.conferenceTimeRepository = conferenceTimeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ConferenceTime timeSix = new ConferenceTime(LocalTime.of(18, 00));
        ConferenceTime timeTen = new ConferenceTime(LocalTime.of(22, 00));
        ConferenceTime six = conferenceTimeRepository.save(timeSix);
        ConferenceTime ten = conferenceTimeRepository.save(timeTen);
    }
}
