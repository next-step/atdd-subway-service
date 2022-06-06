package nextstep.subway.path.finder;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    PathResponse findShortPath(Station sourceStation, Station targetStation);
}
