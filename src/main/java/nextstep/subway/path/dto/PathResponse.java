package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private final List<StationResponse> stations;
	private final int distance;
	private final int price;

	private PathResponse(final List<StationResponse> stations, final int distance, final int price) {
		this.stations = stations;
		this.distance = distance;
		this.price = price;
	}

	public static PathResponse from(Path shortestPath) {
		return new PathResponse(StationResponse.listOf(shortestPath.getStations())
			, shortestPath.getDistance(), shortestPath.getPrice().getIntPrice());
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getPrice() {
		return price;
	}
}
