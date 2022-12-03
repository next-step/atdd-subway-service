package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class DijkstraShortestPathStrategy implements PathStrategy {

    private DijkstraShortestPath path;

    @Override
    public void createGraphPath(Graph graph) {
        this.path = new DijkstraShortestPath(graph);
    }

    @Override
    public GraphPath getGraphPath(Station source, Station target) {
        return path.getPath(source, target);
    }
}
