package nextstep.subway.path.ui;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findPath(@Valid @RequestBody PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findPath(pathRequest));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException() {
        return ResponseEntity.badRequest()
                .build();
    }
}
