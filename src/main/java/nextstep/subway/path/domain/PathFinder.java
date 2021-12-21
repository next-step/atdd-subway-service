package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    public static final String ERROR_PATH_NOT_FOUND = "경로를 찾을 수 없습니다.";
    public static final String ERROR_DUPLICATE_STATION = "출발역과 도착역이 같습니다.";
    public static final String ERROR_START_STATION_NOT_FOUND = "출발역이 찾을 수 없습니다.";
    public static final String ERROR_END_STATION_NOT_FOUND = "도착역을 찾을 수 없습니다.";

    private final SubwayGraph subwayGraph;

    public PathFinder(List<Line> lines) {
        this.subwayGraph = new SubwayGraph(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        checkStations(source, target);

        GraphPath<Station, SectionEdge> graphPath = DijkstraShortestPath.findPathBetween(subwayGraph, source, target);

        if (isNotFoundPath(graphPath)) {
            throw new IllegalArgumentException(ERROR_PATH_NOT_FOUND);
        }

        return new Path(graphPath.getVertexList(), (int)graphPath.getWeight());
    }

    private void checkStations(Station source, Station target) {
        if (isEquals(source, target)) {
            throw new IllegalArgumentException(ERROR_DUPLICATE_STATION);
        }

        if (isNotFoundStation(source)) {
            throw new IllegalArgumentException(ERROR_START_STATION_NOT_FOUND);
        }

        if (isNotFoundStation(target)) {
            throw new IllegalArgumentException(ERROR_END_STATION_NOT_FOUND);
        }
    }

    private boolean isNotFoundStation(Station station) {
        return !subwayGraph.containsVertex(station);
    }

    private boolean isEquals(Station source, Station target) {
        return source.equals(target);
    }

    private boolean isNotFoundPath(GraphPath<Station, SectionEdge> graphPath) {
        return Objects.isNull(graphPath);
    }

}
