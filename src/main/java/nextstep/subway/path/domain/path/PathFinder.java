package nextstep.subway.path.domain.path;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public interface PathFinder {
    GraphPath findPath(Station source, Station target);
}
