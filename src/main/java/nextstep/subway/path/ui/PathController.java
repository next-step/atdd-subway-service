package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private PathService pathService;

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
