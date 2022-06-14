package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    public PathService() {
    }

    public PathResponse findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        ShortestPathFinder shortestPathFinder = new DijkstraShortestPathFinder(lines);
        Path path = shortestPathFinder.getPath(sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
