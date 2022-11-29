package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<Station> stations, int distance, Fare fare) {
        this.stations = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        this.distance = distance;
        this.fare = fare.get();
    }

    private PathResponse() {}

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }
}
