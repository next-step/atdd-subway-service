package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.LinePathQueryService;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final LinePathQueryService linePathQueryService;

    public PathController(LinePathQueryService linePathQueryService) {
        this.linePathQueryService = linePathQueryService;
    }

    @GetMapping
    public ResponseEntity<LinePathResponse> paths(@AuthenticationPrincipal LoginMember loginMember, LinePathRequest linePathRequest) {
        return ResponseEntity.ok(
                linePathQueryService.findShortDistance(loginMember, linePathRequest)
        );
    }
}
