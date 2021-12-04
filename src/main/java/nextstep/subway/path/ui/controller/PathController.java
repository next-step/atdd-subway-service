package nextstep.subway.path.ui.controller;

import nextstep.subway.path.ui.application.PathService;
import nextstep.subway.path.ui.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : nextstep.subway.path.ui.controller
 * fileName : PathController
 * author : haedoang
 * date : 2021/12/04
 * description : 경로 컨트롤러
 */

@RestController
@RequestMapping("paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam(name = "source") Long source, @RequestParam(name = "target") Long target) {
        PathResponse response = pathService.getPath(source, target);
        return ResponseEntity.ok().body(response);
    }
}
