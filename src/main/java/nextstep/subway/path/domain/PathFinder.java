package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PathFinder {

    Path findPath(List<Line> lines, Station sourceStation, Station targetStation);
}
