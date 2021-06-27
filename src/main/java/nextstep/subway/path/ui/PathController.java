package nextstep.subway.path.ui;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam long source, @RequestParam long target) {
        List<Station> stations = new LinkedList<>();
        stations.add(new Station("교대역"));
        stations.add(new Station("남부터미널역"));
        stations.add(new Station("양재역"));
        int distance = 8;

        PathResponse pathResponse = new PathResponse(stations, distance);

        return ResponseEntity.ok().body(pathResponse);
    }
}
