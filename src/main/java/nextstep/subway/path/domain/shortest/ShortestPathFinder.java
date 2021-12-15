package nextstep.subway.path.domain.shortest;

import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {
	void addEdge(Station station1, Station station2, int weight);
	ShortestPath find(Station source, Station target);
}
