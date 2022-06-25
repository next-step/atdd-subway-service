package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {

    PathResponse findShortestPath(Station sourceStation, Station targetStation);
}
