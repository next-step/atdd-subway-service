package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathFindService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final StationService stationService;
    private final PathFindService pathFindService;

    public PathController(PathFindService pathFindService, StationService stationService) {
        this.pathFindService = pathFindService;
        this.stationService = stationService;
    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getDijkstraShortestPath(@RequestParam Long source,
                                                                @RequestParam Long target) {
        Station start = stationService.findById(source);
        Station end = stationService.findById(target);
        PathResponse response = pathFindService.findShortestPath(start, end);
        return ResponseEntity.ok(response);
    }
}
