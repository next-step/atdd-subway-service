package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

public class PathResult {

    private Distance distance;
    private List<Station> stations;

    public PathResult(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = new Distance((int) distance);
    }

    public Distance getDistance() {
        return this.distance;
    }

    public List<Station> getStations() {
        return this.stations;
    }
}
