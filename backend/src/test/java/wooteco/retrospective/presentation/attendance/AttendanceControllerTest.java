package wooteco.retrospective.presentation.attendance;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.config.RestDocsConfiguration;
import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.application.attendance.ConferenceTimeService;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.application.dto.MembersDto;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDao;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;
import wooteco.retrospective.presentation.member.MemberController;

@DisplayName("출석부 - Controller 테스트")
@WebMvcTest
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class AttendanceControllerTest {
    public static final ConferenceTime CONFERENCE_TIME_SIX = new ConferenceTime(1L, LocalTime.of(18, 0, 0));
    public static final ConferenceTimeDto CONFERENCE_TIME_DTO_SIX = new ConferenceTimeDto(1L, LocalTime.of(18, 0, 0));
    public static final Member DANI = new Member(1L, "dani");
    public static final MembersDto MEMBERS_DTO = new MembersDto(Collections.singletonList(DANI));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private ConferenceTimeService conferenceTimeService;

    @MockBean
    private ConferenceTimeDao conferenceTimeDao;

    @MockBean
    private MemberController memberController;

    @DisplayName("회고 시간을 등록한다.")
    @Test
    void postTime() throws Exception {
        AttendanceRequest attendanceRequest = insertAttendance();

        mockMvc.perform(post("/api/time")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(attendanceRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("attendance/post"));
    }

    @DisplayName("회고 시간을 삭제한다.")
    @Test
    void deleteTime() throws Exception {
        AttendanceRequest attendanceRequest = insertAttendance();

        mockMvc.perform(delete("/api/time")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(attendanceRequest)))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("attendance/delete"));
    }

    @DisplayName("회고 목록을 조회한다.")
    @Test
    void getAttendances() throws Exception {
        insertAttendance();

        given(conferenceTimeService.findConferenceTimeById(CONFERENCE_TIME_SIX.getId()))
            .willReturn(CONFERENCE_TIME_DTO_SIX);

        given(attendanceService.findAttendanceByTime(CONFERENCE_TIME_DTO_SIX))
            .willReturn(MEMBERS_DTO);

        mockMvc.perform(get("/api/time/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("conferenceTimeId").value(CONFERENCE_TIME_SIX.getId()))
            .andExpect(jsonPath("members.members[0].id").value(DANI.getId()))
            .andExpect(jsonPath("members.members[0].name").value(DANI.getName()))
            .andDo(print())
            .andDo(document("attendance/get"));
    }

    private AttendanceRequest insertAttendance() {

        given(conferenceTimeDao.insert(any(ConferenceTime.class)))
            .willReturn(CONFERENCE_TIME_SIX);

        given(conferenceTimeDao.findById(any(Long.class)))
            .willReturn(Optional.of(CONFERENCE_TIME_SIX));

        AttendanceRequest attendanceRequest = new AttendanceRequest(
            CONFERENCE_TIME_SIX.getId(),
            DANI.getId()
        );

        given(conferenceTimeService.findConferenceTimeById(1L))
            .willReturn(CONFERENCE_TIME_DTO_SIX);

        return attendanceRequest;
    }
}