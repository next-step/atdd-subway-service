package nextstep.subway.path.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
	@GetMapping
	public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
		List<StationResponse> stations = Arrays.asList(
			new StationResponse(7L),
			new StationResponse(6L),
			new StationResponse(2L),
			new StationResponse(3L),
			new StationResponse(4L));
		int distance = 17;
		PathResponse body = new PathResponse(stations, distance);
		return ResponseEntity.ok(body);
	}
}
