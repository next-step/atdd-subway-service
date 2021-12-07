package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class PathResult {
    public static final PathResult EMPTY_PATH = new PathResult(Collections.emptyList(), 0d, 0);
    private final List<Station> vertexList;
    private final double weight;
    private final int fare;

    public PathResult(List<Station> vertexList, double weight, int fare) {
        this.vertexList = vertexList;
        this.weight = weight;
        this.fare = fare;
    }

    public static PathResult emptyPath() {
        return EMPTY_PATH;
    }

    public boolean isEmpty() {
        return vertexList.isEmpty();
    }

    public List<Station> getVertexList() {
        return vertexList;
    }

    public double getWeight() {
        return weight;
    }

    public int getFare() {
        return fare;
    }
}
