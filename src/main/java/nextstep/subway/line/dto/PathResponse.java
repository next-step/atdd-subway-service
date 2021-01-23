package nextstep.subway.line.dto;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;

	protected PathResponse() {
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public PathResponse(final int distance, final StationResponse... stationResponses) {
		this.distance = distance;
		this.stations = Arrays.asList(stationResponses);
	}
}
