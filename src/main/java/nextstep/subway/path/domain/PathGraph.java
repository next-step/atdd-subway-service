package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathGraph {
    private final ShortestPathFinder shortestPathFinder;

    private PathGraph(ShortestPathFinder shortestPathFinder) {
        this.shortestPathFinder = shortestPathFinder;
    }

    public static PathGraph createJgraphPathGraph() {
        return new PathGraph(new JgraphShortestPathFinder());
    }

    public Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation) {
        return shortestPathFinder.findShortestPath(sections, sourceStation, targetStation);
    }

}
