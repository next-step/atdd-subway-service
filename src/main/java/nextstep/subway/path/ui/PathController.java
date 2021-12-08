package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPaths(@RequestParam(name = "source") Long sourceId, @RequestParam(name = "target") Long targetId) {
        return ResponseEntity.ok().body(pathService.findPaths(sourceId, targetId));
    }
}
