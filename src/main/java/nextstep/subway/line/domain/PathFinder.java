package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public void addLines(List<Line> lines) {
        lines.forEach(this::addLine);
    }

    public void addLine(Line line) {
        addVertex(line);
        addEdgeWeight(line);
    }

    public void removeLine(Line line) {
        line.getStations().forEach(graph::removeVertex);
    }

    private void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void addEdgeWeight(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance())
                );
    }

    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = findShortestGraphPath(
                source, target);

        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestGraphPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (graphPath == null) {
            throw new IllegalArgumentException("경로가 없습니다.");
        }
        return graphPath;
    }
}
