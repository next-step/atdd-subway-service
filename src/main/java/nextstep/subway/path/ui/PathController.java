package nextstep.subway.path.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam final long source,
        @RequestParam final long target) {
        final PathResponse pathResponse = pathService.findShortestPath(source, target);

        return ResponseEntity.ok().body(pathResponse);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException(final RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
