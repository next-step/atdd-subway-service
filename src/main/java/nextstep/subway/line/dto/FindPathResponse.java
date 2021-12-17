package nextstep.subway.line.dto;

import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class FindPathResponse {
	private List<StationResponse> stations;
	private int distance;

	public FindPathResponse(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public void setStations(List<StationResponse> stations) {
		this.stations = stations;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
