package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedGraph<String, DefaultEdge> graph;

    public PathFinder(final List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public Path findPath(final Station sourceStation, final Station targetStation) {
        return new Path(Collections.emptyList(), 0);
    }
}
