package nextstep.subway.path.finder;

import java.util.List;

import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
    private final ShortestPathAlgorithm shortestPathAlgorithm;

    public PathFinder(ShortestPathAlgorithm shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public ShortestPath findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        return shortestPathAlgorithm.findShortestPath(lines, sourceStation, targetStation);
    }
}
