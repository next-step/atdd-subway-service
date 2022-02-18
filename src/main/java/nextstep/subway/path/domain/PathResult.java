package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathResult {

    private List<Station> stations;
    private int distance;

    private PathResult(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResult of(List<Station> stations, double distance) {
        return new PathResult(stations, (int) distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
