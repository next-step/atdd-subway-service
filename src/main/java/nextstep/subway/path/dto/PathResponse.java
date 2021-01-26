package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
	private List<PathStationResponse> stations;
	private long distance;
	private long fare;

	private PathResponse() {
	}

	public PathResponse(List<PathStationResponse> stations, long distance, long fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(Path path) {
		return new PathResponse(PathStationResponse.newList(path.getStations()), path.getDistance(), path.getFare());
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public long getFare() {
		return fare;
	}
}
