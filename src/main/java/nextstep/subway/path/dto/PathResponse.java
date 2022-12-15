package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.path.domain.Path;

public class PathResponse {

	private final List<PathStationResponse> stations;
	private final int distance;
	private final int fare;

	private PathResponse(List<PathStationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(List<PathStationResponse> stations, int distance, int fare) {
		return new PathResponse(stations, distance, fare);
	}

	public static PathResponse from(Path path, Fare fare) {
		return new PathResponse(
			path.stations()
				.mapToList(PathStationResponse::from),
			path.distance().value(),
			fare.value());

	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
