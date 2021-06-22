package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
	private List<StationResponse> stations;
	private int distance;

	public PathResponse(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public PathResponse() {}

	public static PathResponse of(Path subwayPath) {
		List<StationResponse> stationResponses = StationResponse.listOf(subwayPath.getStations());
		return new PathResponse(stationResponses, subwayPath.sumTotalDistance());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
