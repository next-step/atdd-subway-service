package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int fare;

	public PathResponse() {
	}

	public PathResponse(List<StationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public static PathResponse of(List<Station> stations, int distance, int fare) {
		List<StationResponse> stationResponses = stations.stream().map(StationResponse::of)
			.collect(Collectors.toList());
		return new PathResponse(stationResponses, distance, fare);
	}

	// TODO: 제거 필요
	public static PathResponse of(List<Station> stations, int distance) {
		List<StationResponse> stationResponses = stations.stream().map(StationResponse::of)
			.collect(Collectors.toList());
		return new PathResponse(stationResponses, distance, 0);
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
