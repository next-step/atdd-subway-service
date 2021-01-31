package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.NoDiscountStrategy;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final MemberService memberService;

    public PathController(PathService pathService, MemberService memberService) {
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @GetMapping(value = "")
    public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember, PathRequest pathRequest) {
        if (loginMember.isAnonymous()) {
            return ResponseEntity.ok(pathService.findShortestPath(pathRequest, new NoDiscountStrategy()));
        }
        Member member = memberService.getOne(loginMember.getId());
        return ResponseEntity.ok(pathService.findShortestPath(pathRequest, member.getDiscountStrategy()));
    }

}
