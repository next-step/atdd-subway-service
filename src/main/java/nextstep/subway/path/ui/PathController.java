package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.ShortestPathRequest;
import nextstep.subway.path.dto.ShortestPathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController() {
        pathService = new PathService();
    }

    @GetMapping("/short")
    public ResponseEntity<ShortestPathResponse> getShortestPath(ShortestPathRequest requestDto) {
        return ResponseEntity.ok(pathService.getShortestPath(requestDto));
    }

}
