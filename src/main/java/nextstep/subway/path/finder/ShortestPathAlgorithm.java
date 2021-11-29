package nextstep.subway.path.finder;

import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;

public interface ShortestPathAlgorithm {
    ShortestPath findShortestPath(Station sourceStation, Station targetStation);
}
