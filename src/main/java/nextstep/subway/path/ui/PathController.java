package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    @Autowired
    private PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findShortestPath(source, target));
    }
}
