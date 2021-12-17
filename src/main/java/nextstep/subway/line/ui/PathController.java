package nextstep.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.dto.FindPathRequest;

@RequestMapping("/paths")
@RestController
public class PathController {

	@GetMapping
	public ResponseEntity<Void> getShortestPath(FindPathRequest findPathRequest) {
		return ResponseEntity.ok().build();
	}
}
