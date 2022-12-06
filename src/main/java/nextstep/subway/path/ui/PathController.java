package nextstep.subway.path.ui;

import nextstep.subway.path.applicatipn.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(
            @RequestParam(name = "source") Long sourceId,
            @RequestParam(name = "target") Long targetId
    ) {
        PathResponse response = pathService.getPath(sourceId, targetId);
        return ResponseEntity.ok(response);
    }
}
