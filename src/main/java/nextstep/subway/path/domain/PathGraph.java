package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathGraph {
    private final ShortestPathFinder shortestPathFinder;

    public PathGraph(ShortestPathFinder shortestPathFinder) {
        this.shortestPathFinder = shortestPathFinder;
    }

   public List<Station> findShortestPath(Long startStationId, Long endStationId) {
        return shortestPathFinder.getShortestStations(startStationId, endStationId);
   }

}
