package wooteco.retrospective.presentation.pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;
import wooteco.retrospective.presentation.dto.attendance.AttendanceResponse;
import wooteco.retrospective.presentation.dto.member.MemberLoginRequest;
import wooteco.retrospective.presentation.dto.pair.PairResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PairControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.port = port;

        insertPairs("손너잘");
        insertPairs("다니");
        insertPairs("웨지");
        insertPairs("연우");

        attend(1L, 1L);
        attend(1L, 2L);
        attend(1L, 3L);
        attend(1L, 4L);

        attend(2L, 1L);
        attend(2L, 2L);
    }

    @AfterEach
    void clean() {
        jdbcTemplate.update("DELETE FROM PAIR");
        jdbcTemplate.update("DELETE FROM ATTENDANCE");
        jdbcTemplate.update("DELETE FROM MEMBER");

        jdbcTemplate.update("ALTER TABLE MEMBER ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ATTENDANCE ALTER COLUMN id RESTART WITH 1");
    }

    private MemberTokenDto insertPairs(String name) throws JsonProcessingException {
        MemberLoginRequest request = new MemberLoginRequest(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/api/login")
                .then().log().all()
                .extract().as(MemberTokenDto.class);
    }

    private AttendanceResponse attend(Long timeId, Long memberId) throws JsonProcessingException {
        AttendanceRequest request = new AttendanceRequest(timeId, memberId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/api/time")
                .then().log().all()
                .extract().as(AttendanceResponse.class);
    }

    @Test
    void getPairs() {
        Map<String, Long> params = new HashMap<>();
        params.put("date", LocalDate.of(2020,6, 14).toEpochDay());
        params.put("conferenceTime", LocalDate.of(2020,6, 14).toEpochDay());

        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .body(params)
                .when()
                .get("/pairs")
                .then().log().all()
                .extract();


        List<PairResponse> as = extract.as(List.class);
    }
}