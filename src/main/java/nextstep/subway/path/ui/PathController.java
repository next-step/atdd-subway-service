package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.PathWithFareResponse;
import nextstep.subway.path.service.PathService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathWithFareResponse> findPath(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                                         @RequestParam Long source,
                                                         @RequestParam Long target) {
        return ResponseEntity.ok().body(pathService.findPath(loginMember, source, target));
    }

}
