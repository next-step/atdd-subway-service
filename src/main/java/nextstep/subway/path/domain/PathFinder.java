package nextstep.subway.path.domain;

import nextstep.subway.exception.PathCannotFindException;
import nextstep.subway.exception.StationNotIncludedException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final StationGraph stationGraph;

    public PathFinder(List<Section> sections) {
        stationGraph = new StationGraph(sections);
    }

    public Path getShortestPath(Station source, Station target) {
        validate(source, target);
        return stationGraph.findShortestPath(source, target);
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathCannotFindException();
        }
        if (!stationGraph.containsStation(source) || !stationGraph.containsStation(target)) {
            throw new StationNotIncludedException();
        }
    }
}
