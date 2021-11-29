package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping("/path")
    public ResponseEntity findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok().build();
    }
}
