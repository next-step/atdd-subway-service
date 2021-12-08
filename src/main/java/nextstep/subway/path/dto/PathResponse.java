package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private int distance;
	private List<StationResponse> stations;
	private long price;

	private PathResponse() {
	}

	public PathResponse(int distance, List<StationResponse> stationList) {
		this.distance = distance;
		this.stations = stationList;
	}

	public static PathResponse of(int distance, List<StationResponse> stationList) {
		return new PathResponse(distance, stationList);
	}

	public int getDistance() {
		return distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public long getPrice() {
		return price;
	}
}
