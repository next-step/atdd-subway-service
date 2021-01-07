package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/paths")
public class PathController {

	@GetMapping("")
	public ResponseEntity<PathResponse> showPath(@ModelAttribute PathRequest pathRequest) {
		return ResponseEntity.ok(new PathResponse(Collections.emptyList()));
	}
}
