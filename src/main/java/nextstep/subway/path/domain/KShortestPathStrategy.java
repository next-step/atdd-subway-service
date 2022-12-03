package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;

public class KShortestPathStrategy implements PathStrategy {

    private KShortestPaths path;

    private static int PATH_COMPUTE_COUNT = 100;
    private static int FASTEST_INDEX = 0;

    @Override
    public void createGraphPath(Graph graph) {
        this.path = new KShortestPaths(graph, PATH_COMPUTE_COUNT);
    }

    @Override
    public GraphPath getGraphPath(Station source, Station target) {
        List<GraphPath> paths = path.getPaths(source, target);
        return paths.get(FASTEST_INDEX);
    }
}
