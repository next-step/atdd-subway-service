package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.dto.PathStationResponse;

public class PathResponse {
	private List<PathStationResponse> stations;
	private Long distance;

	protected PathResponse() {
	}

	public PathResponse(List<PathStationResponse> stations, Long distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public Long getDistance() {
		return distance;
	}
}
