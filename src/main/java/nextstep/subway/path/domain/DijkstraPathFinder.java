package nextstep.subway.path.domain;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DijkstraPathFinder implements PathStrategy {
    public Path getShortestPath(final List<Line> lines, final Station source, final Station target) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        validateSameStation(source, target);
        validateFoundBothStations(lines, source, target);
        createEdge(graph, lines);
        createVertex(graph, lines);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);
        final List<Station> stations = getVertexes(shortestPath, source, target);
        final List<Section> sections = findSections(lines, stations);
        return Path.of(getWeight(graphPath).intValue(), sections, stations);
    }

    private Double getWeight(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            return Double.NaN;
        }
        return graphPath.getWeight();
    }

    private void createEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> graph , final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(graph::addVertex);
    }

    private void createVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Line> lines) {
        lines.stream()
                .map(Line::getSectionList)
                .flatMap(Collection::stream)
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getValue()));
    }

    private List<Station> getVertexes(final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath, final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if(Objects.isNull(path)){
            return Collections.emptyList();
        }
        return path.getVertexList();
    }

    private void validateSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new PathSameException();
        }
    }

    private void validateFoundBothStations(final List<Line> lines, final Station sourceStation, final Station targetStation) {
        final List<Station> stations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (!stations.contains(sourceStation) || !stations.contains(targetStation)) {
            throw new NotFoundException();
        }
    }

    private List<Section> findSections(final List<Line> lines, final List<Station> stations) {
        List<Section> sections = lines.stream()
                .flatMap(line -> line.getSectionList()
                        .stream())
                .collect(Collectors.toList());

        return sections.stream()
                .filter(section -> section.isSameSection(stations))
                .collect(Collectors.toList());
    }

}
