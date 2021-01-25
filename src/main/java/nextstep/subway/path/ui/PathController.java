package nextstep.subway.path.ui;

import java.util.Arrays;

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
	public ResponseEntity<PathResponse> findShortestPath(PathRequest pathRequest) {
		StationResponse sourceStation = new StationResponse(pathRequest.getSourceStationId(), "강남역", null, null);
		StationResponse targetStation = new StationResponse(pathRequest.getTargetStationId(), "양재역", null, null);
		PathResponse pathResponse = new PathResponse(Arrays.asList(sourceStation, targetStation), 10);

		return ResponseEntity.ok().body(pathResponse);
	}
}
