package nextstep.subway.path.finder;

import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;

public interface ShortestPathAlgorithm {
    ShortestPath findShortestPath(List<Line> lines, Station sourceStation, Station targetStation);
}
