package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(headers = {HttpHeaders.AUTHORIZATION})
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember, @RequestParam long source, @RequestParam long target) {
        return ResponseEntity.ok(pathService.findPath(loginMember, source, target));
    }
    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam long source, @RequestParam long target) {
        return ResponseEntity.ok(pathService.findPath(source, target));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
