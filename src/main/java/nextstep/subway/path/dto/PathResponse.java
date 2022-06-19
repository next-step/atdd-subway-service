package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, double distance) {
        this.stations = stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());

        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}