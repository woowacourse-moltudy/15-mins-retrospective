package wooteco.retrospective.presentation.dto;

import wooteco.retrospective.application.dto.ConferenceTimeDto;

import java.util.List;
import java.util.stream.Collectors;

public class TimesResponse {
    private final List<TimeResponse> timesResponse;

    public TimesResponse(List<TimeResponse> timeResponses) {
        this.timesResponse = timeResponses;
    }

    public static TimesResponse of(List<ConferenceTimeDto> conferenceTimeDtos) {
        return new TimesResponse(conferenceTimeDtos.stream()
            .map(conferenceTimeDto -> new TimeResponse(conferenceTimeDto.getId(), conferenceTimeDto.getConferenceTime()))
            .collect(Collectors.toList()));
    }

    public List<TimeResponse> getTimesResponse() {
        return timesResponse;
    }
}
