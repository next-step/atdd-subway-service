package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping()
    public ResponseEntity<PathResponse> findTheShortestPath(
            @RequestParam(name = "source") long sourceStationId
            , @RequestParam(name = "target") long targetStationId
            , @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findTheShortestPath(sourceStationId, targetStationId, loginMember));
    }
}
