package nextstep.subway.path.ui;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import nextstep.subway.path.application.*;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.*;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> shortPath(@RequestParam long source, @RequestParam long target) {
        Path path = pathService.shortPath(source, target);
        return ResponseEntity.ok().body(PathResponse.from(path));
    }
}
