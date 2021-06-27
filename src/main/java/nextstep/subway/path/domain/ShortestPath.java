package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidPathSearchingException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class ShortestPath {

    private List<Station> stations;
    private int totalDistance;

    public ShortestPath(GraphPath path) {
        validate(path);
        stations = path.getVertexList();
        totalDistance = (int) path.getWeight();
    }

    private void validate(GraphPath path) {
        if (path == null) {
            throw new InvalidPathSearchingException("최단경로가 Null 입니다.");
        }
    }

    public List<Station> stations() {
        return stations;
    }

    public int totalDistance() {
        return totalDistance;
    }
}
