package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public class PathGraph {
    private final ShortestPathFinder shortestPathFinder;

    public PathGraph(ShortestPathFinder shortestPathFinder) {
        this.shortestPathFinder = shortestPathFinder;
    }

   public Path findShortestPath(Station startStation,Station endStation ) {
        return shortestPathFinder.findShortestPath(startStation, endStation);
   }

}
