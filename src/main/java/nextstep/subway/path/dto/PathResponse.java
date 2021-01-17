package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.dto.PathStationResponse;

public class PathResponse {
	private List<PathStationResponse> stations;
	private Long distance;
	private int fare;

	protected PathResponse() {
	}

	public PathResponse(List<PathStationResponse> stations, Long distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(ShortestPath shortestPath, int age) {
		List<PathStationResponse> pathStations = shortestPath.getStations().stream()
			.map(PathStationResponse::of)
			.collect(Collectors.toList());
		return new PathResponse(pathStations, shortestPath.getDistance(), shortestPath.getFareByAge(age));
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public Long getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
