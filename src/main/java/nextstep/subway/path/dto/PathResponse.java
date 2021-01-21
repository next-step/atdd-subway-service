package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
	private List<PathStationResponse> stations;

	private int distance;

	private int finalFare;

	public PathResponse() {
	}

	public PathResponse(List<Station> stations, int distance, int finalFare) {
		this.stations = PathStationResponse.of(stations);
		this.distance = distance;
		this.finalFare = finalFare;
	}

	public static PathResponse of(List<Station> stations, int distance, int finalFare) {
		return new PathResponse(stations, distance, finalFare);
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFinalFare() {
		return finalFare;
	}
}
