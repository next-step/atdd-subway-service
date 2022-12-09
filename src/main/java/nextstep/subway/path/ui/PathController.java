package nextstep.subway.path.ui;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class PathController {
    private PathService service;

    public PathController(PathService service) {
        this.service = service;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(
            @RequestParam("source") Long source,
            @RequestParam("target") Long target) {
        PathResponse response = service.findShortestPath(source, target);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
