package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class ShortestPath {

    private final int basicFare = 1250;
    private final int basicDistance = 10;

    private List<Station> stations;
    private int totalDistance;
    private int fare;

    public ShortestPath(GraphPath path) {
        validate(path);
        stations = path.getVertexList();
        totalDistance = (int) path.getWeight();
        fare = basicFare + calculateOverFare(totalDistance - basicDistance);
    }

    private void validate(GraphPath path) {
        if (path == null) {
            throw new RuntimeException("최단경로가 Null 입니다.");
        }
    }

    private int calculateOverFare(int distance) {
        if (distance > 0) {
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return 0;
    }

    public List<Station> stations() {
        return stations;
    }

    public int totalDistance() {
        return totalDistance;
    }

    public int fare() {
        return fare;
    }
}
