package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.ShortestPathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/short")
    public ResponseEntity<ShortestPathResponse> getShortestPath(@RequestParam("startingStationId") Long startingStationId, @RequestParam("destinationStationId") Long destinationStationId) {
        return ResponseEntity.ok(pathService.findShortestPath(startingStationId, destinationStationId));
    }

}
