package nextstep.subway.line.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;

    public PathResponse(List<Station> stations, double distance) {
        this.stations = StationResponse.ofList(stations);
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, double distance) {
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
