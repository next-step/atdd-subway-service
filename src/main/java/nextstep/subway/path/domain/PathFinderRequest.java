package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinderRequest {
    private final List<Line> lines;
    private final Station source;
    private final Station target;

    private PathFinderRequest(List<Line> lines, Station source, Station target) {
        this.lines = lines;
        this.source = source;
        this.target = target;
    }

    public static PathFinderRequest from(List<Line> lines, Station source, Station target) {
        return new PathFinderRequest(lines, source, target);
    }

    public List<Line> getLines() {
        return lines;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
