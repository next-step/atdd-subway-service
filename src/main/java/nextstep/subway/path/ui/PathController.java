package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final StationService stationService;

    public PathController(PathService pathService, StationService stationService) {
        this.pathService = pathService;
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResponse shortestPath = pathService.findShortestPath(sourceStation, targetStation);
        return ResponseEntity.ok(shortestPath);
    }
}
