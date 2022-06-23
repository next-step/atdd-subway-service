package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private static final LoginMember NON_LOGIN_MEMBER = new LoginMember();
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.getPath(pathRequest, NON_LOGIN_MEMBER));
    }

    @GetMapping(headers = "authorization")
    public ResponseEntity<PathResponse> getPathWithAuth(@AuthenticationPrincipal LoginMember loginMember,
                                                        PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.getPath(pathRequest, loginMember));
    }
}
