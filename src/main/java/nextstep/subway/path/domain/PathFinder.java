package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private StationGraphStrategy stationGraphStrategy;

    public PathFinder(StationGraphStrategy stationGraphStrategy) {
        this.stationGraphStrategy = stationGraphStrategy;
    }

    public Path findShortestPath(List<Line> lines, Station target, Station source) {
        validateSameSourceAndTarget(source, target);

        return stationGraphStrategy.findShortestPath(lines, target, source);
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if(source.equals(target)) {
            throw new PathException();
        }
    }
}
