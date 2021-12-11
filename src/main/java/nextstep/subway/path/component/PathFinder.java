package nextstep.subway.path.component;

import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
    public PathResponse findPath(Sections sections, Station source, Station target) {
        return null;
    }
}
