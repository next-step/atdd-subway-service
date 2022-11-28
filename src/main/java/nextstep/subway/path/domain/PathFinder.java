package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {

    List<Station> findAllStationsInShortestPath(Station source, Station target);

    int findShortestDistance(Station source, Station target);
}
