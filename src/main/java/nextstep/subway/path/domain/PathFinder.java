package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);

    public Path findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        validCheckStation(sourceStation, targetStation);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = createDijkstraPath(
            sections);
        GraphPath<Station, DefaultWeightedEdge> resultPath = dijkstraShortestPath.getPath(
            sourceStation, targetStation);
        validCheckPath(resultPath);
        return Path.of(resultPath.getVertexList(), (int) resultPath.getWeight());
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraPath(
        List<Section> sections) {
        addSections(weightedMultigraph, sections);
        return new DijkstraShortestPath(weightedMultigraph);
    }

    private void addSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        List<Section> sections) {
        addVertex(graph, sections);
        setEdgeWeight(graph, sections);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        List<Section> sections) {
        sections.forEach(
            section -> graph.setEdgeWeight(addEdge(graph, section), section.getDistance().value()));
    }

    private DefaultWeightedEdge addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        List<Section> sections) {
        List<Station> stations = sections.stream()
            .map(Section::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

    private void validCheckStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 최단 경로를 조회할 수 없습니다.");
        }
    }

    private void validCheckPath(GraphPath<Station, DefaultWeightedEdge> resultPath) {
        if (resultPath == null) {
            throw new IllegalArgumentException("출발역에서 도착역까지의 경로를 찾을 수 없습니다.");
        }
    }
}
