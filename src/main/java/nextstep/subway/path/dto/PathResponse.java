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
	private int fare;

	@Builder
	public PathResponse(List<PathStationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public PathResponse() {
	}

	public static PathResponse of(List<PathStationResponse> pathStationResponses, int distance, int fare) {
		return new PathResponse(pathStationResponses, distance, fare);
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
