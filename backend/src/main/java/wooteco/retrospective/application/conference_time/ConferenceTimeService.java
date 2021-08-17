package wooteco.retrospective.application.conference_time;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;
import wooteco.retrospective.exception.NotFoundTimeException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ConferenceTimeService {
    private final ConferenceTimeRepository conferenceTimeRepository;

    public ConferenceTimeService(ConferenceTimeRepository conferenceTimeRepository) {
        this.conferenceTimeRepository = conferenceTimeRepository;
    }

    public ConferenceTimeDto findConferenceTimeById(long conferenceTimeId) {
        ConferenceTime conferenceTime = conferenceTimeRepository.findById(conferenceTimeId)
            .orElseThrow(NotFoundTimeException::new);

        return new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime());
    }

    public List<ConferenceTimeDto> findAllTime() {
        return conferenceTimeRepository.findAll().stream()
            .map(conferenceTime -> new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime()))
            .collect(Collectors.toList());
    }
}
