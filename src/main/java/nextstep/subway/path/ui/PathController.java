package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {
	private final PathService pathService;

	@GetMapping
	public ResponseEntity<PathResponse> getShortestPath(
		@AuthenticationPrincipal LoginMember loginMember,
		@RequestParam long source,
		@RequestParam long target) {

		PathResponse shortestPath = pathService.getShortestPath(source, target);
		shortestPath.setAge(loginMember.getAge());
		return ResponseEntity.ok().body(shortestPath);
	}

}
