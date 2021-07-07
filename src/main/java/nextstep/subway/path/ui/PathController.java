package nextstep.subway.path.ui;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping()
    public ResponseEntity<PathResponse> findBestPath(PathRequest pathRequest) {

        return ResponseEntity.ok(pathService.findBestPath(pathRequest.getSource(), pathRequest.getTarget()));
    }
}
