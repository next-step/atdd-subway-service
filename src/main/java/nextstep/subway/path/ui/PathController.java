package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.NotFoundPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.NotFoundStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") Long sourceId,
                                                         @RequestParam("target") Long targetId) {
        return ResponseEntity.ok().body(pathService.findShortestPath(sourceId, targetId));
    }

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity<String> handleNotFoundStationException(NotFoundStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundPathException.class)
    public ResponseEntity<String> handleNotFoundPathException(NotFoundPathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
