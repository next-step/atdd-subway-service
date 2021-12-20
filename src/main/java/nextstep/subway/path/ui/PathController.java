package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.ControllerExceptionHandler;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/path")
public class PathController extends ControllerExceptionHandler {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(
        @AuthenticationPrincipal final LoginMember loginMember,
        @RequestParam final Long source,
        @RequestParam final Long target) {

        PathResponse pathResponse = pathService
            .findShortestPath(new PathRequest(source, target), loginMember.getId());
        return ResponseEntity.ok().body(pathResponse);
    }

}
