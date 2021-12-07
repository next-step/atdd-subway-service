package nextstep.subway.path.domain;

import java.util.Set;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    PathResponse getShortestPaths(Set<Section> sections, Station sourceStation, Station targetStation);
}
