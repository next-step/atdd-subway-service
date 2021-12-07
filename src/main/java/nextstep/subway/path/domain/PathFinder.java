package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    public final ShortestPathAlgorithm shortestPathAlgorithm;

    private PathFinder(List<Line> lines, ShortestPathAlgorithm shortestPathAlgorithm) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph;

        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> line.addVertexTo(graph));
        lines.forEach(line -> line.setEdgeWeight(graph));

        this.shortestPathAlgorithm = shortestPathAlgorithm;
        this.shortestPathAlgorithm.init(graph);
    }

    public static PathFinder from(List<Line> lines, ShortestPathAlgorithm shortestPathAlgorithm) {
        return new PathFinder(lines, shortestPathAlgorithm);
    }

    public Path shortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPathAlgorithm.getPath(source, target);
        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }
}
