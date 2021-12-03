package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> getShortestPath(
		PathRequest pathRequest
	) {
		return ResponseEntity.ok(pathService.getShortestPath(pathRequest));
	}
}
