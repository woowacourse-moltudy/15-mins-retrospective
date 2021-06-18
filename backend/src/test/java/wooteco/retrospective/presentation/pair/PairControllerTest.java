package wooteco.retrospective.presentation.pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.config.RestDocsConfiguration;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.application.pair.PairService;
import wooteco.retrospective.config.TokenToNameConfig;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.retrospective.common.Fixture.*;

@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = {PairController.class, TokenToNameConfig.class})
public class PairControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PairService pairService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("페어를 요청하면 페어를 반환한다.")
    @Test
    void getPairs() throws Exception {
        List<PairResponseDto> result = List.of(
                new PairResponseDto(new Pair(1L, List.of(neozal, whyguy, duck))),
                new PairResponseDto(new Pair(1L, List.of(neozal, whyguy, duck)))
        );

        given(result);

        ResultActions resultActions = mockMvc.perform(get(
                "/api/pairs?date={date}&conferenceTimeId={conferenceTimeId}",
                "2021-06-15", "1")
                .header(HttpHeaders.AUTHORIZATION, "test_token"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(result)
                ));

        createDocumentForGetPairs(resultActions);
    }

    private void given(List<PairResponseDto> result) {
        BDDMockito.given(pairService.getPairsByDateAndTime(
                any(LocalDate.class),
                any(Long.class),
                any(LocalDateTime.class))
        ).willReturn(result);

        BDDMockito.given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
    }

    private void createDocumentForGetPairs(ResultActions resultActions) throws Exception {
        resultActions.andDo(document("pair/getPairs",
                requestParameters(
                        parameterWithName("date").description("회의 날짜"),
                        parameterWithName("conferenceTimeId").description("회의 시간 Id")
                ),
                responseFields(
                        fieldWithPath("[].pair").type(ARRAY).description("페어 정보"),
                        fieldWithPath("[].pair[].id").type(NUMBER).description("멤버 아이디"),
                        fieldWithPath("[].pair[].name").type(STRING).description("멤버 이름")
                ))
        );
    }
}
