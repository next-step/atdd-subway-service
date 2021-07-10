package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity getShortPaths(@AuthenticationPrincipal LoginMember loginMember, @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findShortestPath(source, target, loginMember);
        return ResponseEntity.ok().body(pathResponse);
    }
}
