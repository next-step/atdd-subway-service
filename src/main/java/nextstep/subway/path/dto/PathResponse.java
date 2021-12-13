package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;

	private PathResponse(List<StationResponse> stationResponses, int distance) {
		this.stations = stationResponses;
		this.distance = distance;
	}

	public static PathResponse of(List<StationResponse> stationResponses, Distance distance) {
		return new PathResponse(stationResponses, distance.value());
	}

	public int getDistance() {
		return distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}
}
