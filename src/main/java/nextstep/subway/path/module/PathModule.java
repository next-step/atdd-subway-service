package nextstep.subway.path.module;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathModule {
    PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation);
}
