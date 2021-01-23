package nextstep.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final LineService lineService;

	public PathController(final LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findPath(@RequestParam final Long source, @RequestParam final Long target) {
		return ResponseEntity.ok(lineService.findPath(source, target));
	}

}
