package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class PathResult {
    public static final PathResult EMPTY_PATH = new PathResult(Collections.emptyList(), 0d);
    private final List<Station> vertexList;
    private final double weight;

    public PathResult(List<Station> vertexList, double weight) {
        this.vertexList = vertexList;
        this.weight = weight;
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
}
