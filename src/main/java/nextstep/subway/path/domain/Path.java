package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.List;

public class Path {

    private static final String IDENTICAL_STATIONS_EXCEPTION = "출발역과 도착역이 동일합니다.";
    private static final String NO_CONNECTION_BETWEEN_STATIONS_EXCEPTION = "출발역과 도착역이 연결되지 않았습니다.";

    private Graph graph = new Graph();
    private List<Station> stations;
    private Distance distance;

    public Path(List<Line> lines, Station source, Station target) {
        validatePath(source, target);
        setGraph(lines);
        setPath(source, target);
    }

    private void validatePath(Station source, Station target) {
        if (source == target) {
            throw new InvalidDataException(IDENTICAL_STATIONS_EXCEPTION);
        }
    }
    private void setGraph(List<Line> lines) {
        lines.stream().forEach(line -> line.getStations().stream()
                .forEach(station -> graph.addVertex(station)));
        lines.stream().forEach(line -> line.getSectionList().stream()
                .forEach(section -> graph.setEdgeWeight(section.getUpStation(), section.getDownStation(), section.getDistance().value())));
    }

    private void setPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph.getGraph());

        try {
            stations = dijkstraShortestPath.getPath(source, target).getVertexList();
            distance = new Distance((int) dijkstraShortestPath.getPath(source, target).getWeight());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(NO_CONNECTION_BETWEEN_STATIONS_EXCEPTION);
        }

    }

    public List<Station> getBestPath() {
        return stations;
    }

    public Distance getBestPathDistance() {
        return distance;
    }
}
