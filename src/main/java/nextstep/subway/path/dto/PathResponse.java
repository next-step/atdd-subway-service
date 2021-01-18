package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;

public class PathResponse {
	private List<PathStationResponse> stations;

	private int distance;

	public PathResponse() {
	}

	public PathResponse(List<Station> stations, int distance) {
		this.stations = PathStationResponse.of(stations);
		this.distance = distance;
	}

	public static PathResponse of(List<Station> stations, int distance) {
		return new PathResponse(stations, distance);
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
