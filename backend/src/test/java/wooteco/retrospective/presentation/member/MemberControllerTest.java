package wooteco.retrospective.presentation.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.dto.member.MemberLoginRequest;
import wooteco.dto.member.MemberLoginResponse;
import wooteco.retrospective.application.member.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 - Controller 테스트")
@WebMvcTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @DisplayName("로그인을 한다. - 정상")
    @Test
    void loginMember() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("dani");
        MemberLoginResponse member = new MemberLoginResponse("dani");

        when(memberService.loginMember(any(MemberLoginRequest.class)))
                .thenReturn(member);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("dani"));
    }

    @DisplayName("로그인을 한다. - 400 예외")
    @Test
    void loginMemberException() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest(" ");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}