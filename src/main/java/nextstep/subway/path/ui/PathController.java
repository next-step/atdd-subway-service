package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.application.PathsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {


    private final PathsService pathsService;

    public PathController(PathsService pathsService) {
        this.pathsService = pathsService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findLinePath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathsService.findPath(source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
