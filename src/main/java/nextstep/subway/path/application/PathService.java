package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

@Service
public class PathService {

	public PathResponse find(Long sourceId, Long targetId) {
		List<StationResponse> stations = new ArrayList<>();
		stations.add(new StationResponse(2L, "양재역", null, null));
		stations.add(new StationResponse(4L, "남부터미널역", null, null));
		stations.add(new StationResponse(3L, "교대역", null, null));
		return new PathResponse(stations, 5);
	}

}
