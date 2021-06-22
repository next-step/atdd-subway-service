package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.math.BigDecimal;
import java.util.List;

public class SubwayNavigation {
    private final ShortestPathAlgorithm<Station, SectionEdge> shortestPath;

    public SubwayNavigation(WeightedGraph graph) {
        this.shortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> getPaths(Station source, Station target) {
        return shortestPath.getPath(source, target).getVertexList();
    }

    public int getDistance(Station source, Station target) {
        return new BigDecimal(shortestPath.getPathWeight(source, target)).intValue();
    }
}
