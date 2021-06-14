package wooteco.retrospective.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.attendance.TimeDao;
import wooteco.retrospective.presentation.attendance.AttendanceController;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

@WebMvcTest(controllers = {AttendanceController.class})
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private TimeDao timeDao;

    @DisplayName("회고 시간을 등록한다.")
    @Test
    void postTime() throws Exception {
        Time newTime = new Time(1L, LocalTime.of(18, 0, 0));

        given(timeDao.insert(any(Time.class)))
            .willReturn(newTime);

        given(timeDao.findById(any(Long.class)))
            .willReturn(Optional.of(newTime));

        AttendanceRequest attendanceRequest = new AttendanceRequest(1L, 1L);
        Attendance attendance = new Attendance(
            LocalDate.now(),
            new Member(1L, "dani"),     //TODO: db로부터 가져오기
            timeDao.findById(1L).orElseThrow(RuntimeException::new)
        );

        given(attendanceService.postAttendance(any(AttendanceRequest.class)))
            .willReturn(attendance);

        mockMvc.perform(post("/api/time")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(attendanceRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("date").value(LocalDate.now().toString()))
            .andExpect(jsonPath("member.name").value("dani"))
            .andExpect(jsonPath("time.time").value("18:00:00"));
    }
}