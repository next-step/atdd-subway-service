package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResultV2;
import nextstep.subway.station.domain.Station;

public interface PathSearch {

    PathResultV2 findShortestPath(List<Line> lines, Station source, Station target);
}

