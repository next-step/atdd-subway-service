package nextstep.subway.path.module;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathModule {
    SubwayPath findPath(List<Line> lines, Station sourceStation, Station targetStation);
}
