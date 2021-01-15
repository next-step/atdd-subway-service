package nextstep.subway.path.dto;

import java.util.List;

import lombok.Builder;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class PathResponse {
	private List<PathStationResponse> stations;
	private int distance;

	@Builder
	public PathResponse(List<PathStationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public PathResponse() {
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
