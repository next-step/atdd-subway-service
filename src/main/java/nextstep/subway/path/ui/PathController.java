package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@ModelAttribute PathRequest pathRequest) {
        PathResponse pathResponse = pathService.findShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok().body(pathResponse);
    }
}
