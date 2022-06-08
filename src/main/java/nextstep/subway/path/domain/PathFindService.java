package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFindService {
    PathFindResult findShortestPath(Station startStation, Station endStation);
}
