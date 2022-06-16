package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity getShortestPath(@RequestBody PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.getShortestPath(pathRequest.getSource(), pathRequest.getTarget()));
    }
}
