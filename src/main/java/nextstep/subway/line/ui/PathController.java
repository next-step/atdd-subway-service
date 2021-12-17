package nextstep.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.PathService;
import nextstep.subway.line.dto.FindPathRequest;
import nextstep.subway.line.dto.FindPathResponse;

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
