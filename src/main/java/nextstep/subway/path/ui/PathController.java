package nextstep.subway.path.ui;

import javax.persistence.EntityNotFoundException;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                         PathRequest request) {
        return ResponseEntity.ok(pathService.findShortestPath(request, loginMember.createDiscountPolicy()));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, IllegalArgumentException.class,
            EntityNotFoundException.class})
    public ResponseEntity handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest()
                .build();
    }
}
