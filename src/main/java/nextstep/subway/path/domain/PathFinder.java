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
    private static DijkstraShortestPath path;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            registerPath(line, graph);
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

    private void registerPath(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addVertices(line, graph);
        addEdges(line, graph);
    }

    private void addVertices(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.findStations()
                .forEach(graph::addVertex);
    }

    private void addEdges(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.getSections()
                .getSections()
                .forEach(section -> addEdgeWith(addEdge(section, graph), section.getDistance(), graph));
    }

    private DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void addEdgeWith(DefaultWeightedEdge edge, int weight, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        graph.setEdgeWeight(edge, weight);
    }
}
