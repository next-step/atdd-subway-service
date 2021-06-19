package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private PathService pathsService;
    private StationService stationService;

    public PathController(PathService pathsService, StationService stationService) {
        this.pathsService = pathsService;
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity updateLine(@RequestParam Long source, @RequestParam Long target) {
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        //

        return ResponseEntity.ok().build();
    }

}
