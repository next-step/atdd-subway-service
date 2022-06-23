package nextstep.subway.path.domain;

import java.util.List;
import java.util.NoSuchElementException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PathFinder {
    private static final String ERROR_MESSAGE_LINES_NULL_OR_EMPTY = "전노선 정보가 비어있거나 null 입니다.";
    private static final String ERROR_MESSAGE_STATION_NULL = "역 정보가 null 입니다.";
    private static final String ERROR_MESSAGE_SAME_SOURCE_AND_TARGET = "출발역과 도착역 정보가 같습니다.";
    private static final String ERROR_MESSAGE_COULD_NOT_FOUND_PATH = "경로를 찾지 못했습니다.";

    public Path findShortestPath(List<Line> allLines, Station source, Station target) {
        validate(allLines, source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = generateStationGraph(allLines);
        return findShortestPath(graph, source, target);
    }

    private void validate(List<Line> allLines, Station source, Station target) {
        validateLines(allLines);
        validateStation(source);
        validateStation(target);
        validateDuplication(source, target);
    }

    private void validateLines(List<Line> allLines) {
        if (CollectionUtils.isEmpty(allLines)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_LINES_NULL_OR_EMPTY);
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

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateStationGraph(List<Line> allLines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allLines.forEach(line -> line.addVertexAndEdgeToGraph(graph));

        return graph;
    }

    private Path findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                  Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);
        validatePathResult(graphPath);

        return convertToPath(graphPath);
    }

    private void validatePathResult(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new NoSuchElementException(ERROR_MESSAGE_COULD_NOT_FOUND_PATH);
        }
    }

    private Path convertToPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
    }
}
