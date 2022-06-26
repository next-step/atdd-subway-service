package nextstep.subway.path.domain;

import nextstep.subway.exception.NoSearchStationException;
import nextstep.subway.line.domain.Line;
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
    private static final String ERROR_MESSAGE_EQUAL_STATION = "출발역과 도착역이 동일합니다.";
    private static final String ERROR_MESSAGE_NOT_CONNECTED = "출발역과 도착역이 연결이 되어 있지 않습니다.";

    public Path findPath(List<Line> lines, Station source, Station target) {
        List<Section> sections = getAllSections(lines);

        validateEqualStation(source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);

        generateGraph(sections, graph);
        validateEmptyStation(graph, source, target);

        GraphPath<Station, DefaultWeightedEdge> graphPath = path.getPath(source, target);
        validateConnected(graphPath);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();

        return Path.of(stations, distance);
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void generateGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance().getDistanceDouble());
        }
    }

    private void validateEqualStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EQUAL_STATION);
        }
    }

    private void validateConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_CONNECTED);
        }
    }

    private void validateEmptyStation(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source, Station target) {
        if (!graph.containsVertex(source)) {
            throw new NoSearchStationException(source.getId());
        }
        if (!graph.containsVertex(target)) {
            throw new NoSearchStationException(target.getId());
        }
    }
}
