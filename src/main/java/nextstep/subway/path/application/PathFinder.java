package nextstep.subway.path.application;

import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    void initGraph(Set<Line> lines);

    Path shortestPath(Station source, Station target);
}
