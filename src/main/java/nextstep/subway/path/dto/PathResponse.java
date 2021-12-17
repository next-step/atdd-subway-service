package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.shortest.ShortestPath;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
	private List<StationResponse> stations;
	private double distance;
	private int fare;

	private PathResponse() { // deserialize
	}

	private PathResponse(List<StationResponse> stations, double distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(ShortestPath shortestPath) {
		final List<StationResponse> stationResponses = shortestPath.getStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		return new PathResponse(stationResponses, shortestPath.getDistance(), shortestPath.getFare());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
