package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class ShortestPath {

    private final List<Station> path;
    private final Distance distance;

    public ShortestPath(List<Station> path, int distance) {
        this.path = path;
        this.distance = new Distance(distance);
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }

    public Distance getDistance() {
        return distance;
    }
}
