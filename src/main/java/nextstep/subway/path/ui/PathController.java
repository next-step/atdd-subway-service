package nextstep.subway.path.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.application.FareService;
import nextstep.subway.fare.domain.DiscountPolicyFactory;
import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final FareService fareService;

    public PathController(final PathService pathService, final FareService fareService) {
        this.pathService = pathService;
        this.fareService = fareService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@AuthenticationPrincipal final LoginMember loginMember,
        @RequestParam final long source,
        @RequestParam final long target) {
        final PathResponse pathResponse = pathService.findShortestPath(source, target);
        final long fare = fareService.calculateFare(pathResponse.getStations(), pathResponse.getDistance(),
            DiscountPolicyFactory.getDiscountPolicy(loginMember));

        return ResponseEntity.ok().body(PathResponse.of(pathResponse, fare));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<Void> handleIllegalArgsException(final RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
