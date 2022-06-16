package nextstep.subway.line.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.PathServiceFacade;
import nextstep.subway.line.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathServiceFacade pathServiceFacade;

    public PathController(PathServiceFacade pathServiceFacade) {
        this.pathServiceFacade = pathServiceFacade;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestParam Long source,
        @RequestParam Long target
    ) {
        PathResponse pathResponse = pathServiceFacade.findPath(source, target, loginMember.getId());
        return ResponseEntity.ok(pathResponse);
    }
}
