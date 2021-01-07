package nextstep.subway.path.ui;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.PathStationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
	@GetMapping
	public ResponseEntity<PathResponse> createStation(PathRequest pathRequest) {
		PathStationResponse station1 = new PathStationResponse(3L, "교대역", LocalDateTime.now());
		PathStationResponse station2 = new PathStationResponse(4L, "남부터미널역", LocalDateTime.now());
		PathStationResponse station3 = new PathStationResponse(2L, "양재역", LocalDateTime.now());
		return ResponseEntity.ok().body(new PathResponse(Arrays.asList(station1, station2, station3), 5L));
	}
}
