package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public interface StationGraphStrategy {
    Path findShortestPath(List<Line> lines, Station source, Station target);
}
