package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    public static WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    public static DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

    public static void addPath(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.distanceToInteger());
    }

    public static List<StationResponse> findShortestPath(Station source, Station target) {
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new RuntimeException(Sections.NOT_FOUND_SECTION);
        }

        return shortestPath.stream().map(Station::toResponse).collect(Collectors.toList());
    }

    public static int findPathWeight(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    public static void removeStation(Section section) {
        graph.removeEdge(section.getUpStation(), section.getDownStation());
    }

    public static void clear() {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }
}
