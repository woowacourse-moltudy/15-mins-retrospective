package wooteco.retrospective.presentation.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.member.MemberService;
import wooteco.retrospective.domain.auth.TokenToName;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.member.MemberLoginRequest;
import wooteco.retrospective.presentation.dto.member.MemberLoginResponse;
import wooteco.retrospective.presentation.dto.member.MemberResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@Valid @RequestBody MemberLoginRequest request) {
        MemberLoginDto requestDto = new MemberLoginDto(request.getName());
        MemberLoginResponse response = new MemberLoginResponse(memberService.loginMember(requestDto).getToken());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/member")
    public ResponseEntity<MemberResponse> findMember(@TokenToName String name) {
        return ResponseEntity.ok(memberService.findMemberByName(name));
    }
}
