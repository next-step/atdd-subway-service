package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {
    Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation);
}
