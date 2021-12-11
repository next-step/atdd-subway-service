package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static nextstep.subway.path.application.exception.InvalidPathException.*;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertexes(line.getStations());
            addEdges(line.getSections());
        }
    }

    private void addVertexes(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(List<Section> sections) {
        sections.forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getWeight());
        });
    }

    public Path shortestPath(Station source, Station target) {
        validateStation(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private void validateStation(Station source, Station target) {
        if (source.equals(target)) {
            throw SAME_DEPARTURE_ARRIVAL;
        }
        if (!isContainsVertex(source, target)) {
            throw NOT_EXIST_STATION;
        }
        if (isNotConnectable(source, target)) {
            throw NOT_CONNECTABLE;
        }
    }

    private boolean isContainsVertex(Station source, Station target) {
        Set<Station> stations = graph.vertexSet();
        return stations.contains(source) && stations.contains(target);
    }

    private boolean isNotConnectable(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Objects.isNull(dijkstraShortestPath.getPath(source, target));
    }
}
