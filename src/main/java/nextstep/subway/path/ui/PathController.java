package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PathController {

    public PathController() {

    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> findPaths(PathRequest pathRequest) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(new StationResponse(1L, "강남역", null, null));
        stations.add(new StationResponse(2L, "양재역", null, null));
        stations.add(new StationResponse(4L, "남부터미널역", null, null));
        PathResponse pathResponse = PathResponse.of(stations, 12L);
        return ResponseEntity.ok().body(pathResponse);
    }
}
