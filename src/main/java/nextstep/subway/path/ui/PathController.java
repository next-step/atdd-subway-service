package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathException;
import nextstep.subway.path.dto.PathResponse;

@RestController
public class PathController {

	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
		return ResponseEntity.ok(pathService.findPath(source, target));
	}

	@ExceptionHandler(PathException.class)
	public ResponseEntity<String> stationExceptionHandler(PathException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
