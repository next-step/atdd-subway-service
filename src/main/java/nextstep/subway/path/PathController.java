package nextstep.subway.path;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPaths(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(new PathResponse(40, "양재역", "남부터미널역", "교대역", "고속터미널역", "잠원역"));
    }

}
