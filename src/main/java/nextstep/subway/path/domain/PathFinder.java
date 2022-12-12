package nextstep.subway.path.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {
    public static Path findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        validateLinesExists(lines);
        validateEqualStations(sourceStation, targetStation);
        GraphPath<Station, Section> path = new DijkstraShortestPath<>(getWeightedMultiGraph(lines))
                .getPath(sourceStation, targetStation);
        validatePathExists(path);

        return Path.of(Distance.from((int) path.getWeight()), path.getVertexList(), lines);
    }

    private static WeightedMultigraph<Station, Section> getWeightedMultiGraph(List<Line> lines) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        lines.forEach(line -> {
            addVertex(graph, line);
            addEdgeAndSetEdgeWeight(graph, line);
        });
        return graph;
    }

    private static void validateLinesExists(List<Line> lines) {
        if (Objects.isNull(lines) || lines.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_FOUND.getMessage());
        }
    }

    private static void validateEqualStations(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_STATION.getMessage());
        }
    }

    private static void validatePathExists(GraphPath<Station, Section> shortestPath) {
        if(Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_FOUND_PATH.getMessage());
        }
    }

    private static void addVertex(WeightedMultigraph<Station, Section> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private static void addEdgeAndSetEdgeWeight(WeightedMultigraph<Station, Section> graph, Line line) {
        line.getSections().forEach(
                section -> {
                    graph.addEdge(section.getUpStation(), section.getDownStation(), section);
                    graph.setEdgeWeight(section, section.getDistance().value());
                });
    }
}
