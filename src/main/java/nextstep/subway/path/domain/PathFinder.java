package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.PathFinderException;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    public static final String ERROR_PATH_NOT_FOUND = "경로를 찾을 수 없습니다.";
    public static final String ERROR_DUPLICATE_STATION = "출발역과 도착역이 같습니다.";
    public static final String ERROR_START_STATION_NOT_FOUND = "출발역이 찾을 수 없습니다.";
    public static final String ERROR_END_STATION_NOT_FOUND = "도착역을 찾을 수 없습니다.";
    public static final String ERROR_GET_LINE_FARE = "노선 추가금 정보 조회 시 오류가 발생했습니다.";

    private final List<Line> lines;
    private final SubwayGraph subwayGraph;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        this.subwayGraph = new SubwayGraph(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        checkStations(source, target);

        GraphPath<Station, SectionEdge> graphPath = DijkstraShortestPath.findPathBetween(subwayGraph, source, target);

        if (isNotFoundPath(graphPath)) {
            throw new IllegalArgumentException(ERROR_PATH_NOT_FOUND);
        }

        return new Path(graphPath.getVertexList(), (int)graphPath.getWeight(),
            getHighestFareAmongLines(graphPath.getEdgeList()));
    }

    private int getHighestFareAmongLines(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .map(sectionEdge -> getLinesFare(sectionEdge.getUpStation(), sectionEdge.getDownStation()))
            .max(Integer::compareTo)
            .orElseThrow(() -> new PathFinderException(ERROR_GET_LINE_FARE));
    }

    private int getLinesFare(Station upStation, Station downStation) {
        return lines.stream()
            .filter(line -> line.isContainsUpStationAndDownStation(upStation, downStation))
            .map(Line::getFare)
            .max(Integer::compareTo)
            .orElseThrow(() -> new PathFinderException(ERROR_GET_LINE_FARE));
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
