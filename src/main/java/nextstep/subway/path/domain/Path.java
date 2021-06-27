package nextstep.subway.path.domain;

import java.util.List;

public class Path {
    private final List stations;
    private final double distance;
    public Path(List stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
