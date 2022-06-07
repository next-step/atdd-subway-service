package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        lines.forEach(PathFinder::registerPath);
    }

    private static void registerPath(Line line) {
        registerEdges(line.getStations());
        registerEdgeWith(line.getSections());
    }

    private static void registerEdges(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    public static void registerEdgeWith(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistanceValue());
        }
    }

    public GraphPath<Station, DefaultWeightedEdge> findPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target);
    }
}
