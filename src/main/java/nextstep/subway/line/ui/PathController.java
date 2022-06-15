package nextstep.subway.line.ui;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.PathResponse;
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
    private final LineService lineService;
    private final StationService stationService;

    public PathController(PathService pathService, LineService lineService,
        StationService stationService) {
        this.pathService = pathService;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(
        @RequestParam Long source,
        @RequestParam Long target
    ) {
        Station sourceStation = stationService.findById(source);
        Station destStation = stationService.findById(target);
        List<Line> lines = lineService.findAllLines();
        PathResponse pathResponse = pathService.findPath(sourceStation, destStation, lines);
        return ResponseEntity.ok(pathResponse);
    }
}
