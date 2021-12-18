package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;

@FunctionalInterface
public interface ShortestPathCalculator {
    PathResponse calculatePath(List<Section> sections, Station sourceStation, Station targetStation);
}
