package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findBestPath(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                       @RequestParam(name = "source") Long sourceId, @RequestParam(name = "target") Long targetId) {
        return ResponseEntity.ok(pathService.findBestPath(loginMember, sourceId, targetId));
    }
}