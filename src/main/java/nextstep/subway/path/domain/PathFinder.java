package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
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
        return Paths.of(DijkstraShortestPath.findPathBetween(graph, source, target));
    }

    public static PathFinder of(List<Line> allLines) {
        if (allLines.isEmpty()) {
            throw new IllegalArgumentException(MUST_BE_NOT_NULL_LINES_ERROR_MESSAGE);
        }

        return new PathFinder(allLines);
    }
}
