package nextstep.subway.path.ui;

import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
        @RequestParam(value = "source") Long sourceStationId,
        @RequestParam(value = "target") Long targetStationId
    ) {
        ShortestPathResponse shortestPath = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(shortestPath);
    }
}
