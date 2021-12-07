package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.CanNotFindPathException;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
		return ResponseEntity.ok(pathService.findPath(pathRequest));
	}

	@ExceptionHandler(CanNotFindPathException.class)
	public ResponseEntity<Void> handleCanNotFindPathException(CanNotFindPathException e) {
		return ResponseEntity.badRequest().build();
	}
}
