package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private final List<StationResponse> stations;
	private final int distance;

	private PathResponse(final List<StationResponse> stations, final int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static PathResponse from(Path shortestPath) {
		return new PathResponse(StationResponse.listOf(shortestPath.getStations())
			, shortestPath.getDistance());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
