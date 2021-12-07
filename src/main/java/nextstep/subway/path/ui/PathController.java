package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : nextstep.subway.path.controller
 * fileName : PathController
 * author : haedoang
 * date : 2021/12/04
 * description : 경로 컨트롤러
 */
@RestController
@RequestMapping("paths")
@RequiredArgsConstructor
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam(name = "source") Long source,
                                                        @RequestParam(name = "target") Long target) {
        PathResponse path = PathResponse.of(pathService.getShortestPath(source, target));
        return ResponseEntity.ok().body(path);
    }
}
