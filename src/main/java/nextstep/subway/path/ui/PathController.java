package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.StationNotConnectException;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.path.exception.StationsSameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public PathResponse findPath(Long source, Long target) {
        return pathService.findPath(source, target);
    }

    @ExceptionHandler({StationsSameException.class, StationNotConnectException.class})
    public ResponseEntity handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({StationNotFoundException.class})
    public ResponseEntity handleNotFound() {
        return ResponseEntity.notFound().build();
    }


}
