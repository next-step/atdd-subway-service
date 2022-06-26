package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {

    private List<Station> stations;
    private Distance distance;

    public ShortestPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
