package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private Distance distance;
    private List<Station> stations = new ArrayList<>();

    public Path() {
    }

    public Path(List<Station> stations, Distance distance) {
        this.distance = distance;
        this.stations = stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
