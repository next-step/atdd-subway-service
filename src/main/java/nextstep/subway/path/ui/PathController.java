package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.line.dto.ShortestPathResponse;
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
    public ResponseEntity findShortestPath(
        @RequestParam(value = "source") Long sourceStationId,
        @RequestParam(value = "target") Long targetStationId
    ) {
        ShortestPathResponse shortestPath = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(shortestPath);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
