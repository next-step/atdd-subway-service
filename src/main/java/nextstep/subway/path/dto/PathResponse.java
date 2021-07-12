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

	public PathResponse(List<Station> stations, int distance) {
		this.stations = getStationResponse(stations);
		this.distance = distance;
	}

	public PathResponse(List<Station> stations, int distance, int fare) {
		this.stations = getStationResponse(stations);
		this.distance = distance;
		this.fare = fare;
	}

	private List<StationResponse> getStationResponse(List<Station> stations) {
		return stations.stream()
			.map(it -> StationResponse.of(it))
			.collect(Collectors.toList());
	}

	public List<StationResponse> getStations() {
		return this.stations;
	}

	public int getDistance(){
		return this.distance;
	}

	public int getFare() {
		return this.fare;
	}
}
