package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private static final String NOT_FOUND_PATH = "경로를 찾지 못하였습니다.";

    private DijkstraShortestPath dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        });
        return new PathFinder(new DijkstraShortestPath(graph));
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target) {
        GraphPath path = dijkstraShortestPath.getPath(source, target);

        if(Objects.isNull(path)) {
            throw new IllegalArgumentException(NOT_FOUND_PATH);
        }
        return path;
    }
}
