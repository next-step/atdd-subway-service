package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private long distance;
	private int fare;

	protected PathResponse() {
	}

	public PathResponse(List<Station> stations, double distance, int fare) {
		this.stations = stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		this.distance = (long)distance;
		this.fare = fare;
	}

	public static PathResponse of(Path path) {
		return new PathResponse(path.getStations(), path.getDistance(), path.getFare());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
