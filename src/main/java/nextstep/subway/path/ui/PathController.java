package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPaths(@AuthenticationPrincipal LoginMember loginMember, @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPaths(loginMember.getId(), source, target));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NoResultException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Void> handleIllegalArgsException() {
        return ResponseEntity.badRequest().build();
    }
}
