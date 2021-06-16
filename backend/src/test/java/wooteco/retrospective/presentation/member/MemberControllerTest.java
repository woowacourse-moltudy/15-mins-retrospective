package wooteco.retrospective.presentation.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.application.member.MemberService;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.member.MemberResponse;
import wooteco.retrospective.utils.auth.JwtTokenProvider;
import wooteco.retrospective.presentation.dto.member.MemberLoginRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 - Controller 테스트")
@WebMvcTest
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    private AttendanceService attendanceService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("로그인을 한다. - 정상")
    @Test
    void loginMember() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("dani");
        MemberTokenDto responseDto = new MemberTokenDto("This_is_JWT_token");

        when(memberService.loginMember(any(MemberLoginDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("This_is_JWT_token"))
                .andDo(print())
                .andDo(document("member/login"));
    }

    @DisplayName("로그인을 한다. - 공백 이름, 400 예외")
    @Test
    void loginMemberFailWithBlankName() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest(" ");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인을 한다. - 빈 이름, 400 예외")
    @Test
    void loginMemberFailWithEmptyName() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인을 한다. - null 이름, 400 예외")
    @Test
    void loginMemberFailWithNullName() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest(null);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("멤버를 찾는다 - 정상")
    @Test
    void findMember() throws Exception {
        Member member = new Member(1L, "pika");

        when(jwtTokenProvider.getPayload(any(String.class)))
                .thenReturn("pika");
        when(jwtTokenProvider.validateToken(any(String.class)))
                .thenReturn(true);
        when(memberService.findMemberByName(any(String.class)))
                .thenReturn(MemberResponse.from(member));

        mockMvc.perform(get("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("pika"))
                .andDo(print())
                .andDo(document("member/find"));
    }

    @DisplayName("멤버를 찾는다 - 유효하지 않는 토큰, 401 예외")
    @Test
    void findMemberWithInvalidToken() throws Exception {
        Member member = new Member(1L, "pika");

        when(jwtTokenProvider.validateToken(any(String.class)))
                .thenReturn(false);

        mockMvc.perform(get("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("member/find"));
    }
}
