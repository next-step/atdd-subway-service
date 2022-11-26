package nextstep.subway.path.domain;

import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathExceptionCode;
import nextstep.subway.path.domain.graph.StationGraph;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final StationGraph stationGraph;

    public PathFinder(List<Section> sections) {
        stationGraph = new StationGraph(sections);
    }

    public Path getShortestPath(Station source, Station target) {
        validate(source, target);
        return findShortestPath(source, target);
    }

    private void validate(Station source, Station target) {
        validateForEqualityOfSourceAndTarget(source, target);
        validateStation(source);
        validateStation(target);
    }

    private void validateForEqualityOfSourceAndTarget(Station source, Station target) {
        if(source.equals(target)) {
            throw new CannotFindPathException(PathExceptionCode.CANNOT_EQUALS_SOURCE_TARGET.getMessage());
        }
    }

    private void validateStation(Station station) {
        if(!stationGraph.containsStation(station)) {
            throw new CannotFindPathException(PathExceptionCode.NO_LINES_CONTAINING_STATION.getMessage());
        }
    }

    private Path findShortestPath(Station source, Station target) {
        return stationGraph.findShortestPath(source, target);
    }
}
