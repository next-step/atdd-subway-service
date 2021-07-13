package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationsResponse;

public class PathResponse {

	private StationsResponse stationsResponse;
	private int distance;

	public static PathResponse of(StationsResponse stationsResponse, int distance) {
		return new PathResponse(stationsResponse, distance);
	}

	public PathResponse() {

	}

	public PathResponse(StationsResponse stationsResponse, int distance) {
		this.stationsResponse = stationsResponse;
		this.distance = distance;
	}


	public StationsResponse getStationsResponse() {
		return stationsResponse;
	}

	public int getDistance() {
		return distance;
	}
}
