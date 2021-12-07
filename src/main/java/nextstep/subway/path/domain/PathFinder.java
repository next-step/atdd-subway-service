package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.CanNotFindPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.UndirectedWeightedGraphBuilderBase;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private static final String MUST_BE_NOT_NULL_LINES_ERROR_MESSAGE = "구간이 최소 하나라도 존재해야 합니다.";
    private static final String SOURCE_AND_TARGET_SAME_ERROR_MESSAGE = "출발역과 도착역을 일치합니다.";
    private static final String STATION_NOT_CONTAINS_ERROR_MESSAGE_FORMAT = "경로파인더의 등록되지 않은 역입니다. [%s]";
    private final UndirectedGraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        final UndirectedWeightedGraphBuilderBase<Station, DefaultWeightedEdge, ? extends WeightedMultigraph<Station, DefaultWeightedEdge>, ?> builder = WeightedMultigraph.builder(DefaultWeightedEdge.class);

        buildVertex(lines, builder);
        buildEdge(lines, builder);

        this.graph = builder.buildUnmodifiable();
    }

    private void buildEdge(List<Line> lines, UndirectedWeightedGraphBuilderBase<Station, DefaultWeightedEdge, ? extends WeightedMultigraph<Station, DefaultWeightedEdge>, ?> builder) {
        List<Section> sections = lines.stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        sections.forEach(section -> builder.addEdge(section.getUpStation(), section.getDownStation(), section.getDistance()));
    }

    private void buildVertex(List<Line> lines, UndirectedWeightedGraphBuilderBase<Station, DefaultWeightedEdge, ? extends WeightedMultigraph<Station, DefaultWeightedEdge>, ?> builder) {
        final List<Station> stations = lines.stream()
                .map(Line::getStations)
                .distinct()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        stations.forEach(builder::addVertex);
    }


    public Paths findPathBetween(Station source, Station target) {
        validateFindPathBetWeen(source, target);
        return Paths.of(DijkstraShortestPath.findPathBetween(graph, source, target));
    }

    private void validateFindPathBetWeen(Station source, Station target) {
        if (source.equals(target)) {
            throw new CanNotFindPathException(SOURCE_AND_TARGET_SAME_ERROR_MESSAGE);
        }
        if (!graph.containsVertex(source)) {
            throw new CanNotFindPathException(String.format(STATION_NOT_CONTAINS_ERROR_MESSAGE_FORMAT, source.getName()));
        }
        if (!graph.containsVertex(target)) {
            throw new CanNotFindPathException(String.format(STATION_NOT_CONTAINS_ERROR_MESSAGE_FORMAT, target.getName()));
        }
    }

    public static PathFinder of(List<Line> allLines) {
        if (allLines.isEmpty()) {
            throw new IllegalArgumentException(MUST_BE_NOT_NULL_LINES_ERROR_MESSAGE);
        }

        return new PathFinder(allLines);
    }
}
