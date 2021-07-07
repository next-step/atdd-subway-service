package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getPaths(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                                 @RequestParam(value = "source") Long sourceStationId,
                                                 @RequestParam(value = "target") Long targetStationId) {
        PathResponse pathResponse = pathService.getPaths(loginMember, sourceStationId, targetStationId);
        return ResponseEntity.ok(pathResponse);
    }
}
