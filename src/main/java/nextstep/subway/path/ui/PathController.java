package nextstep.subway.path.ui;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NoResultException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPaths(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(source, target));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity handleInvalidDataException(InvalidDataException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity handleNoResultException(NoResultException e) {
        return ResponseEntity.badRequest().build();
    }

}
