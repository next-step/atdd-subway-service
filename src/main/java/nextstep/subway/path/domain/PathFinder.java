package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private static final String NOT_PATH_CONNECTED = "출발역과 도착역이 연결이 되어 있지 않습니다.";
    private static final String SAME_SOURCE_TARGET = "출발역과 도착역이 같습니다.";

    private DijkstraShortestPath dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, FareWeightedEdge> graph = new WeightedMultigraph(FareWeightedEdge.class);

        sections.forEach(section -> {
            FareWeightedEdge fareWeightedEdge = new FareWeightedEdge(section.getLine().getFare());
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(section.getUpStation(), section.getDownStation(), fareWeightedEdge);
            graph.setEdgeWeight(fareWeightedEdge, section.getDistance());
        });
        return new PathFinder(new DijkstraShortestPath(graph));
    }

    public ShortestPath findShortestPath(Station source, Station target) {
        checkSameStation(source, target);

        GraphPath<Station, FareWeightedEdge> graph = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
            .orElseThrow(() -> new IllegalArgumentException(NOT_PATH_CONNECTED));
        return ShortestPath.of(graph);
    }

    private void checkSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SAME_SOURCE_TARGET);
        }
    }
}
