package nextstep.subway.path.ui;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService service;

    public PathController(PathService service) {
        this.service = service;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(
            @RequestParam("source") Long source,
            @RequestParam("target") Long target) {
        PathResponse response = service.findShortestPath(source, target);
        return ResponseEntity.ok(response);
    }
}
