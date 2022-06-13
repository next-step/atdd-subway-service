package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(Long source, Long target) {
        PathResponse pathResponse = pathService.findShortestPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

}
