package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.path.application.exception.InvalidPathException.*;

public class PathFinder implements PathFinderStrategy {

    private final JgraphtPathFinder graph;

    public PathFinder(List<Line> lines) {
        this.graph = new JgraphtPathFinder(lines);
    }

    @Override
    public Path shortestPath(Station source, Station target) {
        validateStation(source, target);
        return graph.getPath(source, target);
    }

    private void validateStation(Station source, Station target) {
        if (graph.isSameVertex(source, target)) {
            throw SAME_DEPARTURE_ARRIVAL;
        }
        if (graph.isNotContainsVertex(source, target)) {
            throw NOT_EXIST_STATION;
        }
        if (graph.isNotConnectable(source, target)) {
            throw NOT_CONNECTABLE;
        }
    }

    @Override
    public List<Line> lineOnPath(List<Line> lines) {
        return graph.getEdges().stream()
                .map(edge -> findBy(lines, edge))
                .collect(Collectors.toList());
    }

    private Line findBy(List<Line> lines, SectionEdge edge) {
        Station source = (Station) edge.getSource();
        Station target = (Station) edge.getTarget();

        return lines.stream()
                .filter(line -> line.matchSection(source, target))
                .findFirst()
                .orElseThrow(() -> NOT_EXIST_STATION);
    }
}
