package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final LineService lineService;

    public PathController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPaths(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(lineService.findPaths(source, target));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
