package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
public class PathController {

    @GetMapping(value = "/paths")
    ResponseEntity<PathResponse> search(@RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = findShortestRoute(source,target);
        return ResponseEntity.ok().body(pathResponse);
    }

    private PathResponse findShortestRoute(long source, long target) {
        return new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "양재역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(3L, "교대역", LocalDateTime.now(), LocalDateTime.now())
                ),
                5
        );
    }
}
