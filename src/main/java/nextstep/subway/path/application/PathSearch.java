package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;

public interface PathSearch {

    PathResult findShortestPath(List<Line> lines, Station source, Station target);
}

