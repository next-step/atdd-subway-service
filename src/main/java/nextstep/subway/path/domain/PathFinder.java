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
    private final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);

    private PathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            addEdgeAndSetEdgeWeight(line);
        });
    }

    private void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void addEdgeAndSetEdgeWeight(Line line) {
        line.getSections().forEach(
                section -> {
                    graph.addEdge(section.getUpStation(), section.getDownStation(), section);
                    graph.setEdgeWeight(section, section.getDistance().value());
                });
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateEqualStations(sourceStation, targetStation);
        GraphPath<Station, Section> path = new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        validatePathExists(path);

        return Path.of(Distance.from((int) path.getWeight()), path.getVertexList());
    }

    private void validateEqualStations(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_STATION.getMessage());
        }
    }

    private void validatePathExists(GraphPath<Station, Section> shortestPath) {
        if(shortestPath == null) {
            throw new IllegalArgumentException(ErrorMessage.NOT_FOUND_PATH.getMessage());
        }
    }
}
