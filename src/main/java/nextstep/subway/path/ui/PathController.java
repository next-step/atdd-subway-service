package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.FindPathRequest;
import nextstep.subway.path.dto.FindPathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<FindPathResponse> getShortestPath(FindPathRequest findPathRequest) {
		FindPathResponse findPathResponse = pathService.findShortestPath(findPathRequest);
		return ResponseEntity.ok(findPathResponse);
	}
}
