package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.DistanceFarePolicy;
import nextstep.subway.path.domain.FareSalePolicy;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findBestPath(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                                     @RequestParam Long source, @RequestParam Long target) {
        int age = (loginMember != null) ? loginMember.getAge() : FareSalePolicy.ADULT.getOver() + 1;
        return ResponseEntity.ok(pathService.findShortestPath(source, target, age));
    }
}
