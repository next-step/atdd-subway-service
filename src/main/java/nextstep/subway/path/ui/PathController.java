package nextstep.subway.path.ui;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
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
    private final LineService lineService;

    public PathController(PathService pathService, StationService stationService, LineService lineService) {
        this.pathService = pathService;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        List<Line> lines = lineService.findAllLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResponse pathResponse = pathService.findShortestPath(lines, sourceStation, targetStation);
        return ResponseEntity.ok(pathResponse);
    }
}
