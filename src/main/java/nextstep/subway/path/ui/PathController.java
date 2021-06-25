package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity findShortestDistance(@PathVariable Long source, @PathVariable Long target) {
		pathService.findShortestDistance(source, target);
		return ResponseEntity.ok().build();
	}
}
