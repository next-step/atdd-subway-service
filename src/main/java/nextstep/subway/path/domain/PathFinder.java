package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private static DijkstraShortestPath path;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            registerPath(line);
        }

        path = new DijkstraShortestPath(graph);
    }

    public Path findPath(Station startStation, Station endStation) {
        GraphPath path = PathFinder.path.getPath(startStation, endStation);
        validatePath(path);
        return new Path(path.getVertexList(), (int) PathFinder.path.getPathWeight(startStation, endStation));
    }

    private void validatePath(GraphPath path) {
        if (isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }

    private boolean isNull(GraphPath path) {
        return path == null;
    }

    private void registerPath(Line line) {
        addVertices(line);
        addEdges(line);
    }

    private void addVertices(Line line) {
        line.findStations()
                .forEach(station -> graph.addVertex(station));
    }

    private void addEdges(Line line) {
        line.getSections()
                .getSections()
                .forEach(section -> addEdgeWith(addEdge(section), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void addEdgeWith(DefaultWeightedEdge edge, int weight) {
        graph.setEdgeWeight(edge, weight);
    }
}
