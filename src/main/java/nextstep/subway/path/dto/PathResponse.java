package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationsResponse;

public class PathResponse {

	private StationsResponse stationsResponse;
	private int distance;
	private int fare;

	public static PathResponse of(StationsResponse stationsResponse, int distance, int fare) {
		return new PathResponse(stationsResponse, distance, fare);
	}

	public PathResponse() {

	}

	public PathResponse(StationsResponse stationsResponse, int distance, int fare) {
		this.stationsResponse = stationsResponse;
		this.distance = distance;
		this.fare = fare;
	}

	public StationsResponse getStationsResponse() {
		return stationsResponse;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
