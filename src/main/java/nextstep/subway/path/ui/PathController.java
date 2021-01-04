package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.ui.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findShortestPath(
            @RequestParam(name = "source") Long sourceId,
            @RequestParam(name = "target") Long destinationId,
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        PathResponse pathResponse = pathService.findShortestPath(sourceId, destinationId, loginMember);

        return ResponseEntity.ok().body(pathResponse);
    }
}
