package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final StationGraph stationGraph;

    public PathFinder(StationGraph stationGraph) {
        this.stationGraph = stationGraph;
    }

    public static PathFinder create(StationGraph stationGraph, List<Line> lines) {
        return new PathFinder(stationGraph.createGraph(lines));
    }

    public Path findShortestPath(Station fromStation, Station toStation) {
        validate(fromStation, toStation);
        try {
            return stationGraph.getShortestPath(fromStation, toStation);
        }catch (NotFoundException e) {
            throw new InvalidArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private void validate(Station fromStation, Station toStation) {
        validSameStation(fromStation, toStation);
        validContains(fromStation, toStation);
    }

    private void validSameStation(Station fromStation, Station toStation) {
        if (fromStation.equals(toStation)) {
            throw new InvalidArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private void validContains(Station fromStation, Station toStation) {
        if (!(stationGraph.containsVertex(fromStation) && stationGraph.containsVertex(toStation))) {
            throw new NotFoundException("출발역 또는 도착역이 존재하지 않습니다.");
        }
    }

}
