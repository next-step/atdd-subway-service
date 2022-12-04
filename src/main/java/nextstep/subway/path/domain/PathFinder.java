package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public interface PathFinder {

    Path findPath(Station source, Station target);
}
