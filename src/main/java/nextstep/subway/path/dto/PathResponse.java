package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;

    public PathResponse(List<StationResponse> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}