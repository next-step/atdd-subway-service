package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.PathException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                        @RequestBody PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.getShortestPath(pathRequest.getSource(), pathRequest.getTarget(), loginMember.getAge()));
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity handlePathException(PathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
