package nextstep.subway.path.domain;

import java.util.List;
import java.util.NoSuchElementException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PathFinder {
    private static final String ERROR_MESSAGE_SECTIONS_NULL_OR_EMPTY = "구간 정보 목록이 비어있거나 null 입니다.";
    private static final String ERROR_MESSAGE_STATION_NULL = "역 정보가 null 입니다.";
    private static final String ERROR_MESSAGE_SAME_SOURCE_AND_TARGET = "출발역과 도착역 정보가 같습니다.";
    private static final String ERROR_MESSAGE_COULD_NOT_FOUND_PATH = "경로를 찾지 못했습니다.";

    public Path findShortestPath(List<Section> allSections, Station source, Station target) {
        validate(allSections, source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = generateStationGraph(allSections);
        return findShortestPath(graph, allSections, source, target);
    }

    private void validate(List<Section> allSections, Station source, Station target) {
        validateSections(allSections);
        validateStation(source);
        validateStation(target);
        validateDuplication(source, target);
    }

    private void validateSections(List<Section> allSections) {
        if (CollectionUtils.isEmpty(allSections)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_SECTIONS_NULL_OR_EMPTY);
        }
    }

    private void validateStation(Station station) {
        if (station == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE_STATION_NULL);
        }
    }

    private void validateDuplication(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_SAME_SOURCE_AND_TARGET);
        }
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateStationGraph(List<Section> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allSections.forEach(section -> addSectionToGraph(graph, section));

        return graph;
    }

    private Path findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> allSections,
                                  Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);
        validatePathResult(graphPath);

        return convertToPath(allSections, graphPath);
    }

    private void validatePathResult(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new NoSuchElementException(ERROR_MESSAGE_COULD_NOT_FOUND_PATH);
        }
    }

    private void addSectionToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        addVertexToGraph(graph, section.getUpStation());
        addVertexToGraph(graph, section.getDownStation());
        addEdgeToGraph(graph, section);
    }

    private void addVertexToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station station) {
        if (graph.containsVertex(station)) {
            return;
        }

        graph.addVertex(station);
    }

    private void addEdgeToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistanceValue());
    }

    private Path convertToPath(List<Section> allSections, GraphPath<Station, DefaultWeightedEdge> graphPath) {
        List<Station> stations = graphPath.getVertexList();
        int totalDistance = (int) graphPath.getWeight();
        int maxExtraCharge = getMaxExtraCharge(allSections, stations);

        Charge charge = new Charge(totalDistance, maxExtraCharge);
        return new Path(stations, totalDistance, charge);
    }

    private int getMaxExtraCharge(List<Section> allSections, List<Station> stations) {
        int maxValue = 0;

        for (int i = 0; i < stations.size() - 1; i++) {
            Section section = findSection(allSections, stations.get(i), stations.get(i + 1));

            maxValue = Math.max(maxValue, section.getExtraCharge());
        }

        return maxValue;
    }

    private Section findSection(List<Section> allSections, Station upStation, Station downStation) {
        return allSections.stream()
                .filter(section -> section.hasStation(upStation) && section.hasStation(downStation))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
