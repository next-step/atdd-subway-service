package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/

@RestController
@RequestMapping("/paths")
public class PathController {

	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity findShortestPath(@PathVariable Long sourceId, @PathVariable Long targetId) {
		return ResponseEntity.ok(pathService.findShortestPath(sourceId, targetId));
	}
}
