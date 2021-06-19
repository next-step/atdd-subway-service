package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findPaths(@RequestParam(name = "source") Long sourceStationId, @RequestParam(name = "target") Long targetStationId) {
        PathResponse response = pathService.findPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class,
            NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
