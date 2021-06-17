package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class ShortestPath {

    private final List<Station> path;
    private final int distance;

    public ShortestPath(List<Station> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
