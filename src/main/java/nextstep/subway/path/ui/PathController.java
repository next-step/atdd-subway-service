package nextstep.subway.path.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

	@GetMapping
	public ResponseEntity findPath(@RequestParam Long source, @RequestParam Long target) {
		List<StationResponse> stations = new ArrayList<>();
		stations.add(new StationResponse(2L, "양재역", null, null));
		stations.add(new StationResponse(4L, "남부터미널역", null, null));
		stations.add(new StationResponse(3L, "교대역", null, null));
		PathResponse pathResponse = new PathResponse(stations, 5);
		return ResponseEntity.ok().body(pathResponse);
	}

}
