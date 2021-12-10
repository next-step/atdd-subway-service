package nextstep.subway.path.ui;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(StationResponse.of(new Station("강남역")));
        stations.add(StationResponse.of(new Station("양재역")));
        stations.add(StationResponse.of(new Station("남부터미널역")));
        return ResponseEntity.ok(new PathResponse(stations, 12));
    }
}
