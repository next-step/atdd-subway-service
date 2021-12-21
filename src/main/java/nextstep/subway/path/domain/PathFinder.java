package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private PathStrategy pathStrategy;

    public void setPathStrategy(PathStrategy pathStrategy) {
        this.pathStrategy = pathStrategy;
    }

    public Path findShortestPath(final List<Line> lines, final Station source, final Station target) {
        return pathStrategy.getShortestPath(lines, source, target);
    }
}