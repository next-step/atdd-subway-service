package nextstep.subway.path.domain;

import nextstep.subway.consts.ErrorMessage;
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
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public Path findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_START_END_SAME);
        }
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = createDijkstraPath(sections);
        GraphPath<Station, DefaultWeightedEdge> resultPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (resultPath == null) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_START_END_NOT_FIND);
        }
        return Path.of(resultPath.getVertexList(), (int) resultPath.getWeight());
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraPath(List<Section> sections) {
        addSections(weightedMultigraph, sections);
        return new DijkstraShortestPath(weightedMultigraph);
    }

    private void addSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        addVertex(graph, sections);
        setEdgeWeight(graph, sections);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(addEdge(graph, section), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        List<Station> stations = sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

}
