package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity extractShortestPath(@RequestParam(value = "source") Long sourceId, @RequestParam(value = "target") Long targetId) {
        System.out.println("sourceId : " + sourceId);
        System.out.println("targetId : " + targetId);
        return ResponseEntity.ok().build();
    }
}
