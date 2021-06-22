package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class SubwayNavigation {
    private static final String EQUAL_STATION_EXCEPTION = "같은 역으로 경로를 조회할 수 없습니다.";
    private static final String DISCONNECT_STATION_EXCEPTION = "노선이 연결되어 있지 않습니다.";
    private static final String UNKNOWN_STATION_EXCEPTION = "기존에 등록되지 않은 역입니다.";
    private static final String NO_GRAPH_EXCEPTION = "경로 조회를 위한 객체 그래프 값이 없습니다.";

    private final ShortestPathAlgorithm<Station, SectionEdge> shortestPath;

    public SubwayNavigation(WeightedGraph graph) {
        validateGraph(graph);
        this.shortestPath = new DijkstraShortestPath<>(graph);
    }

    private void validateGraph(WeightedGraph graph) {
        if (Objects.isNull(graph)) {
            throw new IllegalArgumentException(NO_GRAPH_EXCEPTION);
        }
    }

    public List<Station> getPaths(Station source, Station target) {
        validateUnknownStaton(source, target);
        validateEqualStation(source, target);
        GraphPath<Station, SectionEdge> path = shortestPath.getPath(source, target);
        validatePath(path);
        return path.getVertexList();
    }

    private void validateEqualStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(EQUAL_STATION_EXCEPTION);
        }
    }

    private void validatePath(GraphPath<Station, SectionEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException(DISCONNECT_STATION_EXCEPTION);
        }
    }

    private void validateUnknownStaton(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException(UNKNOWN_STATION_EXCEPTION);
        }
    }

    public int getDistance(Station source, Station target) {
        return BigDecimal.valueOf(shortestPath.getPathWeight(source, target)).intValue();
    }
}
