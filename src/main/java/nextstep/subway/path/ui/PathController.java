package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;

@RestController
public class PathController {

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
		return ResponseEntity.ok(PathResponse.createMock());
	}

}
