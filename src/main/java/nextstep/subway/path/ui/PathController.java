package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.apllication.PathService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPath(
                                   @RequestParam("source") Long source,
                                   @RequestParam("target") Long target) {
        LoginMember loginMember  = null;
        return ResponseEntity.ok(pathService.findPath(loginMember, source, target));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
