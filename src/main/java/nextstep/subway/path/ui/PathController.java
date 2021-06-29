package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.NoSuchPathException;
import nextstep.subway.path.exception.SameEndpointException;
import nextstep.subway.station.exception.NoSuchStationException;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam Long source, @RequestParam Long target) {
        PathResponse shortestPath = pathService.getShortestPath(loginMember, source, target);
        return ResponseEntity.ok(shortestPath);
    }

    @ExceptionHandler(value = {
        SameEndpointException.class, NoSuchStationException.class, NoSuchPathException.class})
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
