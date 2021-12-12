package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
	private List<StationResponse> stations;
	private double distance;

	private PathResponse() { // deserialize
	}

	private PathResponse(List<StationResponse> stations, double distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static PathResponse of(ShortestPath shortestPath) {
		final List<StationResponse> stationResponses = shortestPath.getStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		return new PathResponse(stationResponses, shortestPath.getDistance());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}
}
