package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
//        List<StationResponse> stations = new ArrayList<>();
//        stations.add(StationResponse.of(new Station("강남역")));
//        stations.add(StationResponse.of(new Station("양재역")));
//        stations.add(StationResponse.of(new Station("남부터미널역")));
        PathResponse pathResponse = pathService.findPath(source, target);
        return ResponseEntity.ok(pathResponse);

    }
}
