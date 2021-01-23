package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathFindService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathFindService pathFindService;

    public PathController(PathFindService pathFindService) {
        this.pathFindService = pathFindService;
    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getDijkstraShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                                @RequestParam Long source,
                                                                @RequestParam Long target) {
        PathResponse response = pathFindService.findShortestPathAndFare(loginMember, source, target);
        return ResponseEntity.ok(response);
    }
}