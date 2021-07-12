package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.LoginStatus;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.AgePolicy;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                    @RequestParam Long source, @RequestParam Long target) {
        AgePolicy agePolicy = AgePolicy.NONE;
        if (loginMember.getStatus().equals(LoginStatus.LOGIN)) {
            agePolicy = AgePolicy.valueOf(loginMember.getAge());
        }
        return ResponseEntity.ok().body(pathService.findPath(source, target, agePolicy));
    }
}
