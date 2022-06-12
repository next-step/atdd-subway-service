package nextstep.subway.path.ui;

import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.path.application.PathService;
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
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(source, target));
    }

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity handleError(ErrorCodeException e) {
        return ResponseEntity.badRequest().build();
    }
}
