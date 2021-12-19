package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;

@RestController
public class PathController {

	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPaths(@RequestBody PathRequest pathRequest, @AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(pathService.findPaths(pathRequest, loginMember));
	}
}
