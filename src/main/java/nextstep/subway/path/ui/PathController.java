package nextstep.subway.path.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping(value = "/paths", produces = MediaType.APPLICATION_JSON_VALUE)
public class PathController {

	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
		@RequestParam("source") Long source, @RequestParam("target") Long target) {
		PathResponse pathResponse = pathService.findShortestPath(loginMember, source, target);
		return ResponseEntity.ok(pathResponse);
	}
}
