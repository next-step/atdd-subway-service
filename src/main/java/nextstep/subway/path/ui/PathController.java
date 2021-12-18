package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findShortestPath(@RequestParam Long source, @RequestParam Long target) {
//        PathResponse response = PathResponse.of(
//                Arrays.asList(
//                        PathResponse.StationResponse.of(3L,"A", LocalDateTime.now()),
//                        PathResponse.StationResponse.of(4L,"B", LocalDateTime.now()),
//                        PathResponse.StationResponse.of(2L,"C", LocalDateTime.now())
//                ), 5
//        );
        return ResponseEntity.ok(pathService.findShortestPath(source, target));
    }
}
