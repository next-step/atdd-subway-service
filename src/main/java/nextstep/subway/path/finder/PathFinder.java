package nextstep.subway.path.finder;

import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final ShortestPathAlgorithm shortestPathAlgorithm;

    private PathFinder(ShortestPathAlgorithm shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public static PathFinder from(ShortestPathAlgorithm shortestPathAlgorithm) {
        return new PathFinder(shortestPathAlgorithm);
    }

    public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
        return shortestPathAlgorithm.findShortestPath(sourceStation, targetStation);
    }
}
