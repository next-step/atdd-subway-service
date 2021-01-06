package nextstep.subway.path.ui;

import javax.validation.Valid;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<?> paths(@RequestBody @Valid PathRequest request) {
        PathResponse shortestPath = pathService.getShortestPath(request);
        return ResponseEntity.ok(shortestPath);
    }

}
