package nextstep.subway.line.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.dto.path.PathRequest;
import nextstep.subway.line.dto.path.PathResponse;
import nextstep.subway.member.domain.Age;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "anonymous", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> anonymousSearch(PathRequest pathRequest) {
        return ResponseEntity.ok(
            pathService.getShortestPath(pathRequest.getSource(), pathRequest.getTarget(),
                Age.DEFAULT_AGE));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> search(@AuthenticationPrincipal LoginMember loginMember,
        PathRequest pathRequest) {
        return ResponseEntity.ok(
            pathService.getShortestPath(pathRequest.getSource(), pathRequest.getTarget(),
                loginMember.getAge()));
    }
}
