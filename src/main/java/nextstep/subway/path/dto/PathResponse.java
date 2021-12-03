package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private final List<StationResponse> stations;
	private final Distance distance;

	public PathResponse(final List<StationResponse> stations, final Distance distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance.getDistance();
	}
}
