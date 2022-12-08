package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;

public class PathResponse {

	private List<PathStationResponse> stations;
	private int distance;

	private PathResponse() {
	}

	private PathResponse(List<PathStationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static PathResponse of(List<PathStationResponse> stations, int distance) {
		return new PathResponse(stations, distance);
	}

	public static PathResponse from(Path path) {
		return new PathResponse(
			path.stations()
				.mapToList(PathStationResponse::from),
			path.distance().value());

	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
