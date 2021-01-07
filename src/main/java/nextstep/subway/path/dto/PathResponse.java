package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

	private List<StationResponse> stations;

	public PathResponse() {
	}

	public static PathResponse of(List<Station> stations) {
		List<StationResponse> responses = stations.stream()
				.map(StationResponse::of)
				.collect(Collectors.toList());
		return new PathResponse(responses);
	}

	public PathResponse(List<StationResponse> stations) {
		this.stations = stations;
	}

	public List<StationResponse> getStations() {
		return stations;
	}
}
