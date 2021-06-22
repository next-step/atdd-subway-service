package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final MemberService memberService;

    public PathController(PathService pathService, MemberService memberService) {
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity findPath(@AuthenticationPrincipal LoginMember loginMember, @RequestParam Long source, @RequestParam Long target) {
        if (loginMember.getId() != null) {
            MemberResponse member = memberService.findMember(loginMember.getId());
            return ResponseEntity.ok(pathService.findPath(source, target));
        }
        return ResponseEntity.ok(pathService.findPath(source, target));
    }

}
