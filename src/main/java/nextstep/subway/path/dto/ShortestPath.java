package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private List<Station> stations;
    private int distance;

    public ShortestPath() {
    }

    public ShortestPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
