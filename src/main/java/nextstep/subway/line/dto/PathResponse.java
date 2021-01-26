package nextstep.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.SubwayPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int fare;

	protected PathResponse() {
	}

	public static PathResponse of(final SubwayPath subwayPath, final Fare fare) {
		return new PathResponse(convertStationResponses(subwayPath.getStations()), subwayPath.getDistance(),
			fare.getFare());
	}

	private static List<StationResponse> convertStationResponses(final List<Station> stations) {
		return stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	public PathResponse(final List<StationResponse> stations, final int distance, final int fare) {
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
