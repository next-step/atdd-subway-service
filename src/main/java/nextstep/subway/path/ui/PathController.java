package nextstep.subway.path.ui;

import nextstep.subway.path.PathResponse;
import nextstep.subway.path.application.PathService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<PathResponse> get(@RequestParam long source, @RequestParam long target) {
        return ResponseEntity.ok(pathService.get(source, target));
    }
}
