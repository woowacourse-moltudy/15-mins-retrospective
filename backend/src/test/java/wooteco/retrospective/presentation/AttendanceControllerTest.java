package wooteco.retrospective.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDao;
import wooteco.retrospective.presentation.attendance.AttendanceController;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

@DisplayName("출석부 - Controller 테스트")
@WebMvcTest(controllers = {AttendanceController.class})
@AutoConfigureRestDocs
class AttendanceControllerTest {
    public static final ConferenceTime CONFERENCE_TIME_SIX = new ConferenceTime(1L, LocalTime.of(18, 0, 0));
    public static final Member DANI = new Member(1L, "dani");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private ConferenceTimeDao conferenceTimeDao;

    @DisplayName("회고 시간을 등록한다.")
    @Test
    void postTime() throws Exception {
        AttendanceRequest attendanceRequest = insertAttendance();

        mockMvc.perform(post("/api/time")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(attendanceRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("date").value(LocalDate.now().toString()))
            .andExpect(jsonPath("member.name").value("dani"))
            .andExpect(jsonPath("conferenceTime.conferenceTime").value("18:00:00"))
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
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("attendance/delete"));
    }

    @DisplayName("회고 목록을 조회한다.")
    @Test
    void getAttendances() throws Exception {
        insertAttendance();
        List<Member> members = Collections.singletonList(DANI);

        given(attendanceService.findTimeById(CONFERENCE_TIME_SIX.getId()))
            .willReturn(CONFERENCE_TIME_SIX);

        given(attendanceService.findAttendanceByTime(CONFERENCE_TIME_SIX))
            .willReturn(members);

        mockMvc.perform(get("/api/time/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("conferenceTimeId").value(CONFERENCE_TIME_SIX.getId()))
            .andExpect(jsonPath("members[0].id").value(DANI.getId()))
            .andExpect(jsonPath("members[0].name").value(DANI.getName()))
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

        Attendance attendance = new Attendance(
            LocalDate.now(),
            DANI,     //TODO: db로부터 가져오기
            conferenceTimeDao.findById(CONFERENCE_TIME_SIX.getId()).orElseThrow(RuntimeException::new)
        );

        given(attendanceService.postAttendance(any(AttendanceRequest.class)))
            .willReturn(attendance);
        return attendanceRequest;
    }
}