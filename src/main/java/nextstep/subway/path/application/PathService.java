package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
public class PathService {
	public PathResponse findPath(Long sourceId, Long targetId) {
		List<StationResponse> stationResponses = new ArrayList<>();
		stationResponses.add(StationResponse.of(new Station("남부터미널역")));
		stationResponses.add(StationResponse.of(new Station("교대역")));
		stationResponses.add(StationResponse.of(new Station("강남역")));
		return new PathResponse(stationResponses, 7);
	}
}
