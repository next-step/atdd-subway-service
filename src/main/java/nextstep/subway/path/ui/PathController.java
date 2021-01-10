package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                         @RequestParam long source,
                                                         @RequestParam long target) {

        PathResponse pathResponse = Objects.isNull(loginMember) ?
                pathService.findShortest(source, target) : pathService.findShortestWithFee(source, target, loginMember);
        return ResponseEntity.ok(pathResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
