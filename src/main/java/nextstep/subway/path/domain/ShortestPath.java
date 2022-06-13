package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class ShortestPath {
    private final GraphPath<Station, DefaultWeightedEdge> elements;

    public ShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station upStation, Station downStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        elements = dijkstraShortestPath.getPath(upStation, downStation);
    }

    public List<Station> getPath() {
        Objects.requireNonNull(elements, "출발역과 도착역이 연결되어 있지 않습니다.");
        return elements.getVertexList();
    }
}
