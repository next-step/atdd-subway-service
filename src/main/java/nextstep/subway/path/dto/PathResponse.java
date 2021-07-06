package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.StationPath;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int fare;

	public PathResponse(List<StationResponse> stations, int distance, int fare) {
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

	public static PathResponse of(StationPath path, Fare fare) {
		return new PathResponse(StationResponse.of(path.getVertexList()), (int)path.getDistance(), fare.value());
	}
}
