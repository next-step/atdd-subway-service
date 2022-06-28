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

    private final DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath;
    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;

    public DijkstraShortestPathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        addVertexes(lines);
        addEdgeWeight(lines);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
    }

    @Override
    public Path findShortestPath(Station source, Station target) {
        validateEqualsSourceAndTarget(source, target);
        GraphPath<Station, SectionWeightedEdge> graphPath = this.dijkstraShortestPath.getPath(source, target);
        validateNoneMatchPath(graphPath);
        return Path.of(findPathIncludeLines(graphPath), graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void addVertexes(List<Line> lines) {
        Set<Station> allStations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        allStations.forEach(this.graph::addVertex);
    }

    private void addEdgeWeight(List<Line> lines) {
        Set<Section> allSections = lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        allSections.forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(Section section) {
        SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
        this.graph.addEdge(section.getUpStation(), section.getDownStation(), sectionWeightedEdge);
        this.graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());
    }

    private void validateEqualsSourceAndTarget(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(EQUALS_SOURCE_TARGET);
        }
    }

    private void validateNoneMatchPath(GraphPath<Station, SectionWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException(NONE_MATCH_PATH);
        }
    }

    private List<Line> findPathIncludeLines(GraphPath<Station, SectionWeightedEdge> graphPath) {
        return findPathSections(graphPath).stream()
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Section> findPathSections(GraphPath<Station, SectionWeightedEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                .map(SectionWeightedEdge::getSection)
                .collect(Collectors.toList());
    }
}
