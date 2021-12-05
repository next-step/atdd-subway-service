package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class PathResult {
    private final List<Station> vertexList;
    private final double weight;

    public PathResult(List<Station> vertexList, double weight) {
        this.vertexList = vertexList;
        this.weight = weight;
    }

    public static PathResult emptyPath() {
        return new PathResult(Collections.emptyList(), 0d);
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
}
