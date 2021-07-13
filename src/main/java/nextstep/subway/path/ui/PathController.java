package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathsRequest;
import nextstep.subway.path.dto.PathsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathsResponse> shortestPath(@RequestParam(name = "source") Long source, @RequestParam(name = "target") Long target) {
        return ResponseEntity.ok(pathService.findShortestPath(source, target));
    }
}
