package nextstep.subway.path.ui;


import java.util.List;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.ErrorResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathFinderService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathFinderController {

    private final PathFinderService pathFinderService;

    public PathFinderController(final PathFinderService pathFinderService) {
        this.pathFinderService = pathFinderService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathFinderService.getShortestPath(source, target));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, IllegalStateException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", 400, e.getMessage()));
    }


}
