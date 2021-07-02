package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
	private final PathFinder pathFinder;

	public PathController(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(
			@RequestParam(name = "source") Long sourceStationId,
			@RequestParam(name = "target") Long targetStationId
	) {
		return ResponseEntity.ok(pathFinder.find(sourceStationId, targetStationId));
	}
}
