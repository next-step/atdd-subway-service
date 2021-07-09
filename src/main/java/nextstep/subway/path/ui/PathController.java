package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {
	private PathService pathService;

	public PathController(PathService pathservice) {
		this.pathService = pathservice;
	}

	@GetMapping
	public ResponseEntity findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
		PathResponse pathResponse = pathService.findPath(sourceId, targetId);

		return ResponseEntity.ok().body(pathResponse);
	}
}
