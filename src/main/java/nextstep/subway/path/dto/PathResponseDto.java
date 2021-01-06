package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponseDto {
	private List<StationResponse> stations;
	private int distance;

	public PathResponseDto(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
