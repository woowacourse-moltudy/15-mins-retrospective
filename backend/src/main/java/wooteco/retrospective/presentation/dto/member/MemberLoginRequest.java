package wooteco.retrospective.presentation.dto.member;

import javax.validation.constraints.NotBlank;

public class MemberLoginRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private MemberLoginRequest() {
    }

    public MemberLoginRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
