package nextstep.subway.path.domain.shortest;

import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {
	ShortestPath find(Station source, Station target);
}
