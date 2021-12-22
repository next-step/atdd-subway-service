package nextstep.subway.path.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.FindShortestPathResult;
import nextstep.subway.station.dto.StationResponse;

public class FindPathResponse {
	private List<StationResponse> stations;
	private int distance;

	public FindPathResponse(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static FindPathResponse of(FindShortestPathResult findShortestPathResult) {
		List<StationResponse> path = findShortestPathResult.getStations()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		int distance = findShortestPathResult.getDistance();

		return new FindPathResponse(path, distance);
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public void setStations(List<StationResponse> stations) {
		this.stations = stations;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FindPathResponse that = (FindPathResponse)o;
		return distance == that.distance && Objects.equals(stations, that.stations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stations, distance);
	}
}
