package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
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

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getShortestPaths(@RequestParam(value = "source") int source,
                                                         @RequestParam(value = "target") int target) {
        Path shortestPath = pathService.findShortestPath(source, target);
        return ResponseEntity.ok().body(PathResponse.of(shortestPath));
    }

}
