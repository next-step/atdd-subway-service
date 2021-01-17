package nextstep.subway.path.ui;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;

@RestController
public class PathController {

	@GetMapping(value = "/paths", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
		PathResponse pathResponse = new PathResponse(
			Arrays.asList(
				new PathStationResponse(3L, "교대역", LocalDateTime.now()),
				new PathStationResponse(4L, "남부터미널역", LocalDateTime.now()),
				new PathStationResponse(2L, "양재역", LocalDateTime.now())
			),
			5L
		);
		return ResponseEntity.ok().body(pathResponse);
	}
}
