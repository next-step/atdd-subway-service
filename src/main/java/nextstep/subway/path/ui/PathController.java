package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam final Long sourceId, @RequestParam final Long targetId) {
        return ResponseEntity.ok(pathService.findShortestPath(sourceId, targetId));
    }
}
