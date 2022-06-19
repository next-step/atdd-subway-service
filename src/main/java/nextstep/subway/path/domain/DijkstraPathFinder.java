package nextstep.subway.path.domain;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DijkstraPathFinder implements PathFinder{
    @Override
    public Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation) {
        validateInputStations(sourceStation, targetStation);
        DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath = buildDijkstraPath(sections);
        GraphPath<Station, SectionWeightedEdge> resultPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateResult(resultPath);
        return Path.of(collectSections(resultPath), (int) resultPath.getWeight());
    }

    private DijkstraShortestPath buildDijkstraPath(List<Section> sections) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        registerSections(graph, sections);
        return new DijkstraShortestPath(graph);
    }

    private void registerSections(WeightedMultigraph<Station, SectionWeightedEdge> routeGraph, List<Section> sections) {
        addVertexes(routeGraph, sections);
        addWeightedEdges(routeGraph, sections);
    }

    private void addVertexes(WeightedMultigraph<Station, SectionWeightedEdge> routeGraph, List<Section> sections) {
        List<Station> stations = sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        stations.forEach(station -> routeGraph.addVertex(station));
    }

    private void addWeightedEdges(WeightedMultigraph<Station, SectionWeightedEdge> routeGraph, List<Section> sections) {
        sections.forEach(section -> {
                    SectionWeightedEdge edge = addEdge(routeGraph, section);
                    routeGraph.setEdgeWeight(edge, section.getDistance().value());
                    edge.registerSection(section);
                }
        );
    }

    private SectionWeightedEdge addEdge(WeightedMultigraph<Station, SectionWeightedEdge> routeGraph, Section section) {
        return routeGraph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void validateInputStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_SAME_SOURCE_TARGET);
        }
    }

    private void validateResult(GraphPath<Station, SectionWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_NOT_FOUND);
        }
    }

    private List<Section> collectSections(GraphPath<Station, SectionWeightedEdge> resultPath) {
        return resultPath.getEdgeList()
                .stream()
                .map(edge -> edge.getSection())
                .collect(Collectors.toList());
    }
}
