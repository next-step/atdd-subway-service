package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
	private List<PathStationResponse> stations;

	private PathResponse() {
	}

	public PathResponse(List<PathStationResponse> stations) {
		this.stations = stations;
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}
}
