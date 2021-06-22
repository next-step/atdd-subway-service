package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.math.BigDecimal;
import java.util.List;

public class SubwayNavigation {
    private static final String EQUAL_STATION_EXCEPTION = "같은 역으로 경로를 조회할 수 없습니다.";

    private final ShortestPathAlgorithm<Station, SectionEdge> shortestPath;

    public SubwayNavigation(WeightedGraph graph) {

        this.shortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> getPaths(Station source, Station target) {
        validateEqualStation(source, target);
        return shortestPath.getPath(source, target).getVertexList();
    }

    private void validateEqualStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(EQUAL_STATION_EXCEPTION);
        }
    }

    public int getDistance(Station source, Station target) {
        return new BigDecimal(shortestPath.getPathWeight(source, target)).intValue();
    }
}
