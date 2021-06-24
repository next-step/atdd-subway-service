package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class SubwayShortestPath {
    private final List<Station> stations;
    private final int distance;

    public SubwayShortestPath(List<Station> stations, int distance) {
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
