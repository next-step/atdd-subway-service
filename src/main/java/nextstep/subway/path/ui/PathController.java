package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity findShortestPath(@RequestParam long source, @RequestParam long target) {
        return ResponseEntity.ok().build();
    }
}
