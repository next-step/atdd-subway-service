package nextstep.subway;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
//        configureGraph(lines);
        configureGraphNew(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void configureGraph(List<Line> lines) {
        lines.forEach(line -> addVertexes(line));
        lines.forEach(line -> setEdgeWeights(line));
    }

    private void addVertexes(Line line) {
        line.getStationsInOrder()
                .forEach(station -> graph.addVertex(station));
    }

    private void setEdgeWeights(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()));
    }

    private void configureGraphNew(List<Line> lines) {
        lines.forEach(line -> line.configure(graph));
    }

    public GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
        isSame(source, target);
        isExistent(source, target);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역은 연결되어 있어야 합니다.");
        }

        return path;
    }

    private void isSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 달라야 합니다.");
        }
    }

    private void isExistent(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }
}
