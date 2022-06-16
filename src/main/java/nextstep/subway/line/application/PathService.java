package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    public PathResponse findPath(Station sourceStation, Station destStation, List<Line> lines) {
        PathFinder pathFinder = new PathFinder(lines);

        GraphPath path = pathFinder.findPath(sourceStation, destStation);

        return PathResponse.of(path.getVertexList(), path.getWeight());
    }
}
