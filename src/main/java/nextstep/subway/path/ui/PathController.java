package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.ShortestPathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity findShortestPath(@RequestParam("source") Long sourceStationId,
                                           @RequestParam("target") Long targetStationId,
                                           @AuthenticationPrincipal LoginMember loginMember) {
        ShortestPathResponse response = pathService.findShortestPath(sourceStationId, targetStationId, loginMember);
        return ResponseEntity.ok().body(response);
    }
}
