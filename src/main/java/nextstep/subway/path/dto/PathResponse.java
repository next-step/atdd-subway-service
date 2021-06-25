package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private long distance;

	protected PathResponse() {
	}

	public PathResponse(List<Station> stations, double distance) {
		this.stations = stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		this.distance = (long)distance;
	}

	public static PathResponse of(Path path) {
		return new PathResponse(path.getStations(), path.getDistance());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}
}
