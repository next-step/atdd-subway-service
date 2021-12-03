package nextstep.subway.path.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping(value = "/paths",produces = MediaType.APPLICATION_JSON_VALUE)
public class PathController {

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") String source, @RequestParam("target") String target) {
		return null;
	}
}
