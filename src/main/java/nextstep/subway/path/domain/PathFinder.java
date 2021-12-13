package nextstep.subway.path.domain;

import java.util.Set;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    PathFinderResponse getShortestPaths(Set<Section> sections, Station sourceStation, Station targetStation);
}
