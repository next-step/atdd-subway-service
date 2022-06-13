package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;


public class PathFinder {
    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private static DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public static void addLines(List<Line> lines) {
        if (Objects.isNull(graph)) {
            graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
            dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        }
        lines.forEach(PathFinder::addLine);
    }

    public static void addLine(Line line) {
        addVertex(line);
        addEdgeWeight(line);
    }

    private static void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private static void addEdgeWeight(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance())
                );
    }

    public static Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = findShortestGraphPath(
                source, target);

        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private static GraphPath<Station, DefaultWeightedEdge> findShortestGraphPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로가 없습니다.");
        }
        return graphPath;
    }
}
