package nextstep.subway.line.domain;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    public PathResponse findPath(List<Line> lines, Station source, Station target) {
        return null;
    }
}
