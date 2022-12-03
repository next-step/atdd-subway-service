package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final StationGraph stationGraph;

    private PathFinder(StationGraph stationGraph) {
        this.stationGraph = stationGraph;
    }

    public static PathFinder of(StationGraph stationGraph) {
        return new PathFinder(stationGraph);
    }

    public Path findPathFromGraph(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalStateException(ErrorMessage.FIND_PATH_SAME_STATION);
        }
        return stationGraph.findPath(source, target);
    }
}
