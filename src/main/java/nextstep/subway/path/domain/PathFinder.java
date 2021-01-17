package nextstep.subway.path.domain;

import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final StationGraph stationGraph;
    private final PathAlgorithm pathAlgorithm;

    public PathFinder(StationGraph stationGraph, PathAlgorithm pathAlgorithm) {
        this.stationGraph = stationGraph;
        this.pathAlgorithm = pathAlgorithm;
    }

    public Path findShortestPath(Station source, Station target) {
        validate(source, target);

        List<Station> shortestPath = pathAlgorithm.getShortestPath(source, target);
        int distance = pathAlgorithm.getDistance(source, target);
        int fare = FareCalculator.calculateFare(distance, stationGraph.getSurchargesOfLine(shortestPath));

        return new Path(shortestPath, distance, fare);
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidFindShortestPathException("출발역과 도착역이 같으면 조회 불가능합니다.");
        }
        if (isNotContainStation(source, target)) {
            throw new InvalidFindShortestPathException("출발역이나 도착역이 존재하지 않습니다.");
        }
        if (isNotConnectStations(source, target)) {
            throw new InvalidFindShortestPathException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    private boolean isNotContainStation(Station source, Station target) {
        return stationGraph.isNotContainStation(source, target);
    }

    private boolean isNotConnectStations(Station source, Station target) {
        return pathAlgorithm.getPath(source, target) == null;
    }
}
