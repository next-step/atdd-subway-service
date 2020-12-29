package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {
	private final PathService pathService;

	@GetMapping
	public ResponseEntity<PathResponse> getShortestPath(@RequestParam long source, @RequestParam long target) {

		return ResponseEntity.ok().body(pathService.getShortestPath(source, target));
	}

}
