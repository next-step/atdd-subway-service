package nextstep.subway.station.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

public class StationsResponse {

    private List<StationResponse> stations;

	public static StationsResponse of(List<Station> stations) {
		List<StationResponse> stationResponses = stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		return new StationsResponse(stationResponses);
	}

    public StationsResponse() {
    }

    public StationsResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

	public List<StationResponse> getStations() {
		return stations;
	}
}
