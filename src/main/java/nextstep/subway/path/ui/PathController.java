package nextstep.subway.path.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;
    private final AuthService authService;

    public PathController(PathService pathService, AuthService authService) {
        this.pathService = pathService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortCut(
            @AuthenticationPrincipal(required = false) LoginMember loginMember,
            @RequestParam("source") Long sourceStationId,
            @RequestParam("target") Long targetStationId) {
        PathResponse pathResponse = pathService.getShortCut(sourceStationId, targetStationId, loginMember);
        return ResponseEntity.ok(pathResponse);
    }
}
