package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    public PathResponse findShortestPath(List<Line> lines, Station source, Station target) {
        return null;
    }
}
