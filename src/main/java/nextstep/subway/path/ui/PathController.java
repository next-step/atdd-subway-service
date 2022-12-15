package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.applicatipn.PathService;
import nextstep.subway.path.dto.PathResponse;
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
    public ResponseEntity<PathResponse> getPath(
            @AuthenticationPrincipal(required = false) LoginMember loginMember,
            @RequestParam(name = "source") Long sourceId,
            @RequestParam(name = "target") Long targetId
    ) {
        PathResponse response = pathService.getPath(sourceId, targetId, loginMember);
        return ResponseEntity.ok(response);
    }
}
