package nextstep.subway.line.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPaths(@AuthenticationPrincipal LoginMember loginMember,
                                                  @RequestParam Long source,
                                                  @RequestParam Long target) {
        PathResponse response = pathService.findPaths(loginMember, source, target);
        return ResponseEntity.ok(response);
    }
}
