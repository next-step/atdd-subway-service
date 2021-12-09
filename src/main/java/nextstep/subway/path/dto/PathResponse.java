package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stationResponses;
	private int distance;

	public PathResponse(List<StationResponse> stationResponses, int distance) {
		this.stationResponses = stationResponses;
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public List<StationResponse> getStationResponses() {
		return stationResponses;
	}
}
