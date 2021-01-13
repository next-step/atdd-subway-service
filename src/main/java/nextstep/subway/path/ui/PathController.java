package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source,
                                                         @RequestParam Long target,
                                                         @AuthenticationPrincipal LoginMember loginMember) {
        PathResponse pathResponse = pathService.findShortestPath(source, target, loginMember);
        return ResponseEntity.ok(pathResponse);
    }
}
