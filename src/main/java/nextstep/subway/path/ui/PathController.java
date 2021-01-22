package nextstep.subway.path.ui;

import nextstep.subway.auth.application.LoginMemberValidator;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathFinder pathFinder;
    private final LoginMemberValidator loginMemberValidator;

    public PathController(PathFinder pathFinder, LoginMemberValidator loginMemberValidator) {
        this.pathFinder = pathFinder;
        this.loginMemberValidator = loginMemberValidator;
    }

    /**
     * 출발역-도착역으로 가는 최단 거리 경로를 구합니다.
     * @param sourceStationId
     * @param targetStationId
     * @return
     */
    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getPaths(@AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(value = "source") Long sourceStationId, @RequestParam(value = "target") Long targetStationId) {
        if(this.validateLoginMember(loginMember)) {
            return ResponseEntity.badRequest().build();
        }

        PathResponse pathResponse = this.pathFinder.getShortestPath(loginMember, sourceStationId, targetStationId);
        return ResponseEntity.ok().body(pathResponse);
    }

    /**
     * 로그인 된 사용자 정보의 유효성 체크를 합니다.
     * @param loginMember
     * @return
     */
    private boolean validateLoginMember(LoginMember loginMember) {
        Errors errors = new BeanPropertyBindingResult(loginMember, "loginMember");
        this.loginMemberValidator.validate(loginMember, errors);
        return errors.hasErrors();
    }
}
