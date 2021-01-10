package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

	private final List<StationResponse> stations;
	private final int distance;
	private final int fare;

	public PathResponse() {
		this.stations = new ArrayList<>();
		this.distance = 0;
		this.fare = 0;
	}

	public static PathResponse of(List<Station> stations) {
		List<StationResponse> responses = stations.stream()
				.map(StationResponse::of)
				.collect(Collectors.toList());
		return new PathResponse(responses, 0, 0);
	}

	public PathResponse(List<StationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
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
