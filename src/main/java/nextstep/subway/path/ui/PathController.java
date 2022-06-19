package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> paths(@AuthenticationPrincipal LoginMember loginMember,
                                              @RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        PathResponse response = pathService.findShortestDistancePath(loginMember.getAge(), sourceId, targetId);
        return ResponseEntity.ok().body(response);
    }

}
