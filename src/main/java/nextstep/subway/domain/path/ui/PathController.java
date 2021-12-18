package nextstep.subway.domain.path.ui;

import nextstep.subway.domain.auth.domain.AuthenticationPrincipal;
import nextstep.subway.domain.auth.domain.LoginMember;
import nextstep.subway.domain.path.application.PathService;
import nextstep.subway.domain.path.dto.PathFinderRequest;
import nextstep.subway.domain.path.dto.PathFinderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathFinderResponse> findPath(PathFinderRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findPaths(request, loginMember));
    }
}
