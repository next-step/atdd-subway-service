package nextstep.subway.path.ui;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NoSuchDataException;
import nextstep.subway.path.application.PathService;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity findBestPath(@RequestParam(name = "source") Long sourceId, @RequestParam(name = "target") Long targetId) {
        return ResponseEntity.ok(pathService.findBestPath(sourceId, targetId));
    }

    @ExceptionHandler({NoSuchDataException.class, InvalidDataException.class, DataIntegrityViolationException.class})
    public ResponseEntity handleDataIntegrityViolationException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}