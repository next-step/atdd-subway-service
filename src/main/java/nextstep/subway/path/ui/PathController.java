package nextstep.subway.path.ui;

import javax.validation.Valid;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<?> paths(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody @Valid PathRequest request) {
        PathResponse shortestPath = pathService.getShortestPath(request, loginMember);
        return ResponseEntity.ok(shortestPath);
    }

}
