package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@RestController
public class PathController {

	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity findShortestPath(@RequestParam(value = "source") Long sourceId, @RequestParam(value = "target") Long targetId) {
		return ResponseEntity.ok(pathService.findShortestPath(sourceId, targetId));
	}
}
