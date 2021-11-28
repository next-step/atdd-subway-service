package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class ShortestPath {
    private List<StationResponse> stations = new ArrayList<>();
    private int distance;

    public ShortestPath() {}

    private ShortestPath(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortestPath of(List<StationResponse> stations, int distance) {
        return new ShortestPath(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
