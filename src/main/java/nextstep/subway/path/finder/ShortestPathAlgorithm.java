package nextstep.subway.path.finder;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public interface ShortestPathAlgorithm {
    PathResponse findShortestPath(Station sourceStation, Station targetStation);
}
