package wooteco.retrospective.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.retrospective.application.dto.ConferenceTimeDto;

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
