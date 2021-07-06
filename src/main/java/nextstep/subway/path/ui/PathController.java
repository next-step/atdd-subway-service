package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/sourceId/{sourceId}/targetId/{targetId}")
    public ResponseEntity<PathResponse> findOptimalPath(@AuthenticationPrincipal LoginMember loginMember,
                                                        @PathVariable("sourceId") Long sourceId,
                                                        @PathVariable("targetId") Long targetId) {
        return ResponseEntity.ok().body(pathService.findOptimalPath(sourceId, targetId));
    }


}
