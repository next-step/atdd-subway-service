package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> search(@RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = pathService.findShortestRoute(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class , IllegalStateException.class})
    public ResponseEntity handleException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
