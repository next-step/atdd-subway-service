package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DijkstraShortestPathFinder implements PathFinder {
    private static final String EQUALS_SOURCE_TARGET = "출발역과 도착역이 같으면 경로를 찾을 수 없습니다.";
    private static final String NONE_MATCH_PATH = "요청하신 경로를 찾을 수 없습니다.";

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public DijkstraShortestPathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(lines);
        setEdgeWeight(lines);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
    }

    @Override
    public Path findShortestPath(Station source, Station target) {
        validateEqualsSourceAndTarget(source, target);
        GraphPath<Station, DefaultWeightedEdge> graphPath = this.dijkstraShortestPath.getPath(source, target);
        validateNoneMatchPath(graphPath);
        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void addVertexes(List<Line> lines) {
        Set<Station> allStations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        allStations.forEach(this.graph::addVertex);
    }

    private void setEdgeWeight(List<Line> lines) {
        Set<Section> allSections = lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        allSections.forEach(section -> this.graph.setEdgeWeight(
                this.graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );
    }

    private void validateEqualsSourceAndTarget(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(EQUALS_SOURCE_TARGET);
        }
    }

    private void validateNoneMatchPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException(NONE_MATCH_PATH);
        }
    }
}
