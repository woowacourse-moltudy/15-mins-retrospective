package wooteco.retrospective.application.attendance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDao;

@Transactional(readOnly = true)
@Service
public class ConferenceTimeService {
    private final ConferenceTimeDao conferenceTimeDao;

    public ConferenceTimeService(ConferenceTimeDao conferenceTimeDao) {
        this.conferenceTimeDao = conferenceTimeDao;
    }

    public ConferenceTimeDto findConferenceTimeById(long conferenceTimeId) {
        ConferenceTime conferenceTime = conferenceTimeDao.findById(conferenceTimeId)
            .orElseThrow(RuntimeException::new);

        return new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime());
    }
}
