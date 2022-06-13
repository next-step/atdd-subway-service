package nextstep.subway.path.domain;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DijkstraPathFinder implements PathFinder{
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    @Override
    public Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation) {
        initialize(sections);
        validateInputStations(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> resultPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateResult(resultPath);
        return Path.of(resultPath.getVertexList(), (int) resultPath.getWeight());
    }

    private void initialize(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        registerSections(graph, sections);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void registerSections(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, List<Section> sections) {
        addVertexes(routeGraph, sections);
        addWeightedEdges(routeGraph, sections);
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, List<Section> sections) {
        List<Station> stations = sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        stations.forEach(station -> routeGraph.addVertex(station));
    }

    private void addWeightedEdges(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, List<Section> sections) {
        sections.forEach(section -> routeGraph.setEdgeWeight(
                addEdge(routeGraph, section), section.getDistance().value())
        );
    }

    private DefaultWeightedEdge addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Section section) {
        return routeGraph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void validateInputStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_SAME_SOURCE_TARGET);
        }
    }

    private void validateResult(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_NOT_FOUND);
        }
    }
}
