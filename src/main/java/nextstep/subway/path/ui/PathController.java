package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findShortestPath(pathRequest));
    }
}

