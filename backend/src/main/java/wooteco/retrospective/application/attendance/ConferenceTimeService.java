package wooteco.retrospective.application.attendance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.dao.ConferenceTimeDao;
import wooteco.retrospective.exception.NotFoundTimeException;

@Transactional(readOnly = true)
@Service
public class ConferenceTimeService {
    private final ConferenceTimeDao conferenceTimeDao;

    public ConferenceTimeService(ConferenceTimeDao conferenceTimeDao) {
        this.conferenceTimeDao = conferenceTimeDao;
    }

    public ConferenceTimeDto findConferenceTimeById(long conferenceTimeId) {
        ConferenceTime conferenceTime = conferenceTimeDao.findById(conferenceTimeId)
            .orElseThrow(NotFoundTimeException::new);

        return new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime());
    }

    public List<ConferenceTimeDto> findAllTime() {
        return conferenceTimeDao.findAll().stream()
            .map(conferenceTime -> new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime()))
            .collect(Collectors.toList());
    }
}
