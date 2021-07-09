package nextstep.subway.path.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/path")
public class PathController {
	@GetMapping
	public ResponseEntity findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
		List<StationResponse> stationResponses = new ArrayList<>();
		stationResponses.add(StationResponse.of(new Station("남부터미널역")));
		stationResponses.add(StationResponse.of(new Station("교대역")));
		stationResponses.add(StationResponse.of(new Station("강남역")));
		PathResponse pathResponse = new PathResponse(stationResponses, 7);

		return ResponseEntity.ok().body(pathResponse);
	}
}
