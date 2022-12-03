package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;

public class KShortestPathStrategy  implements PathStrategy {
    private KShortestPaths path;

    @Override
    public void createGraphPath(Graph graph) {
        this.path = new KShortestPaths(graph, 100);
    }

    @Override
    public GraphPath getGraphPath(Station source, Station target) {
        List<GraphPath> paths =  path.getPaths(source, target);
        return paths.get(0);
    }
}
