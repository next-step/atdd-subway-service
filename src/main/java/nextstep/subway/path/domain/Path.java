package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> shortestPath;
    private int shortestDistance;

    public Path(List<Station> shortestPath, int shortestDistance) {
        this.shortestPath = shortestPath;
        this.shortestDistance = shortestDistance;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }
}
