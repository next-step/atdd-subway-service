package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                 @ModelAttribute PathRequest request) {
        PathResponse path = pathService.findPath(request);
        int age = loginMember.getAge();
        if (age >= 6 && age < 13) {
            path.setFare((int) ((path.getFare() - 350) * 0.5));
        } else if (age >= 13 && age < 19) {
            path.setFare((int) ((path.getFare() - 350) * 0.8));
        }
        return ResponseEntity.ok(path);
    }
}
