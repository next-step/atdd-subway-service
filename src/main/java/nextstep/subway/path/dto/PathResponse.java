package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathPrice;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private int distance;
	private List<StationResponse> stations;
	private int price;

	private PathResponse() {
	}

	public PathResponse(int distance, List<StationResponse> stations, int price) {
		this.distance = distance;
		this.stations = stations;
		this.price = price;
	}

	public static PathResponse of(Path path) {
		List<StationResponse> stationResponses = path.getStationsRoute()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		return new PathResponse(path.getDistance(), stationResponses, path.getPrice());
	}

	public int getDistance() {
		return distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getPrice() {
		return price;
	}
}
