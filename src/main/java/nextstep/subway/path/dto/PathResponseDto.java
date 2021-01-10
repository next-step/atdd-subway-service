package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponseDto {
	private List<StationResponse> stations;
	private int distance;
	private int fare;

	public PathResponseDto(List<StationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
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
