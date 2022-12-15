package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService){
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortPath(@RequestParam(name = "source") Long source,
                                                      @RequestParam(name = "target") Long target){
        PathResponse response = pathService.findShortPath(source, target);

        return ResponseEntity.ok(response);
    }
}
