package nextstep.subway.path.ui;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.enumerate.AuthenticationType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> searchShortestPath(@AuthenticationPrincipal LoginMember loginMember, @RequestParam Long source, @RequestParam Long target) {
        try {
            if (loginMember.getAuthenticationType() != AuthenticationType.GUEST) {
                return ResponseEntity.ok().body(pathService.searchShortestPath(source, target, loginMember.getAge()));
            }

            return ResponseEntity.ok().body(pathService.searchShortestPath(source, target));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    } 
}
