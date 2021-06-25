package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam(value = "start") int start,
                                                         @RequestParam(value = "end") int end) {
        return ResponseEntity.ok().build();
    }
}
