package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.station.domain.Station;

public class ShortestPath {
    private List<Station> stations = new ArrayList<>();
    private int distance;

    public ShortestPath() {}

    private ShortestPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortestPath of(List<Station> stations, int distance) {
        return new ShortestPath(stations, distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
