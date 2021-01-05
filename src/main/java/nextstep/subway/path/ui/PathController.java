package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = pathService.findShortest(source, target);
        return ResponseEntity.ok(pathResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
