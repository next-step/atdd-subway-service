package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {

    List<Station> findAllStationsInTheShortestPath(Station sourceStation, Station targetStation);

    int findTheShortestPath(Station sourceStation, Station targetStation);
}
