package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;

    private PathFinder(WeightedMultigraph<Station, SectionWeightedEdge> graph) {
        this.graph = graph;
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(createGraph(lines));
    }

    public Path findShortestPath(Station source, Station target) {
        validateNotEqual(source, target);
        validateExists(source, target);

        GraphPath graphPath = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new RuntimeException("출발역과 도착역이 연결되어 있지 않음"));

        return Path.of(graphPath);
    }

    private static WeightedMultigraph<Station, SectionWeightedEdge> createGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        for (Line line : lines) {
            addVertexesAndEdges(graph, line);
        }
        return graph;
    }

    private static void addVertexesAndEdges(WeightedMultigraph<Station, SectionWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            SectionWeightedEdge edge = new SectionWeightedEdge(section);
            graph.addVertex(edge.getUpStation());
            graph.addVertex(edge.getDownStation());
            graph.addEdge(edge.getUpStation(), edge.getDownStation(), edge);
            graph.setEdgeWeight(edge, edge.getDistance());
        }
    }

    private void validateNotEqual(Station source, Station target) {
        if (source.equals(target)) {
            throw new RuntimeException("출발역과 도착역이 같음");
        }
    }

    private void validateExists(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new RuntimeException("존재하지 않은 출발역이나 도착역을 조회");
        }
    }
}
