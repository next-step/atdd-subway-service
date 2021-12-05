package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/path")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source,
        @RequestParam Long target) {
        return ResponseEntity.ok().build();
    }

}
