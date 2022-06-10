package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
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

    public List<Station> findPath(Station startStation, Station endStation) {
        return path.getPath(startStation, endStation).getVertexList();
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
