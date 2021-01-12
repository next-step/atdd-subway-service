package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.Passenger;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathFinderResponse;
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
    public ResponseEntity<PathFinderResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                               @RequestParam long source, @RequestParam long target) {

        final Passenger passenger = Passenger.getPassengerType(loginMember.getAge());
        return ResponseEntity.ok(pathService.findShortestPath(passenger, source, target));
    }
}
