package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final Station source;
    private final Station target;
    private final List<Line> lines;

    public PathFinder(Station source, Station target, List<Line> lines) {
        this.source = source;
        this.target = target;
        this.lines = lines;
    }
}
