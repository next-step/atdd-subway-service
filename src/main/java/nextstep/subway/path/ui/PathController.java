package nextstep.subway.path.ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPathBySourceAndTarget(@RequestParam Long source,
            @RequestParam Long target) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(new StationResponse(6L, "양재역", LocalDateTime.now(), LocalDateTime.now()));
        stations.add(new StationResponse(7L, "오리역", LocalDateTime.now(), LocalDateTime.now()));
        stations.add(new StationResponse(3L, "선릉역", LocalDateTime.now(), LocalDateTime.now()));
        stations.add(new StationResponse(4L, "잠실역", LocalDateTime.now(), LocalDateTime.now()));
        PathResponse reponse = PathResponse.of(stations, 13);
        //return ResponseEntity.ok(pathService.findPathBySourceAndTarget(source, target));
        return ResponseEntity.ok(reponse);
    }
}
