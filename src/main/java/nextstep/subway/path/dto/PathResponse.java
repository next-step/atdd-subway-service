package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
	private List<StationResponse> stations;
	private int distance;
	private int fare;

	public PathResponse(List<StationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(Path path) {
		return new PathResponse(StationResponse.of(path.getStations()), path.getDistance(), path.getFare());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
