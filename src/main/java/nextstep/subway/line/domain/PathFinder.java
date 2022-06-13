package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private static PathFinder pathFinder;
    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private static DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder() {
        addLines(new ArrayList<>());
    }

    public PathFinder(List<Line> lines) {
        addLines(lines);
    }

    private PathFinder(WeightedMultigraph graph) {
        this.graph = graph;
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public PathFinder addLines(List<Line> lines) {

        if (!Objects.isNull(pathFinder)) {
            lines.forEach(pathFinder::addLine);
            return pathFinder;
        }

        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        pathFinder = new PathFinder(graph);

        lines.forEach(pathFinder::addLine);

        return pathFinder;
    }

    public void addLine(Line line) {
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


    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = findShortestGraphPath(
                source, target);

        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestGraphPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로가 없습니다.");
        }
        return graphPath;
    }
}
