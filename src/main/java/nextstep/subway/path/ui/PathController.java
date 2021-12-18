package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity findShortestPath(@RequestParam Long source, @RequestParam Long target) {

        PathResponse response = PathResponse.of(
                Arrays.asList(
                        PathResponse.StationResponse.of(3L,"A", LocalDateTime.now()),
                        PathResponse.StationResponse.of(4L,"B", LocalDateTime.now()),
                        PathResponse.StationResponse.of(2L,"C", LocalDateTime.now())
                ), 5
        );

        return ResponseEntity.ok(response);
    }
}
