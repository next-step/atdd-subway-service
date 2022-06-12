package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private static WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph;

    public PathFinder(List<Line> lines) {
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        return null;
    }

    public static WeightedMultigraph<Station, DefaultWeightedEdge> getRouteGraph() {
        return routeGraph;
    }
}
