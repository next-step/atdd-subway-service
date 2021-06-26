package nextstep.subway.path.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity findShortestDistance(@RequestParam Long source, @RequestParam Long target) {
		PathResponse pathResponse = pathService.findShortestDistance(source, target);
		return ResponseEntity.ok(pathResponse);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleIllegalArgsException(RuntimeException e) {
		return ResponseEntity.badRequest().build();
	}
}
