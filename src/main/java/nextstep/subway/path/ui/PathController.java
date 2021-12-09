package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.exception.InvalidPathException;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam("sourceId") Long sourceId,
                                                         @RequestParam("targetId") Long targetId) {
        PathResponse path = pathService.findPath(sourceId, targetId);
        return ResponseEntity.ok(path);
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity handler(InvalidPathException e) {
        return ResponseEntity.badRequest().build();
    }
}
