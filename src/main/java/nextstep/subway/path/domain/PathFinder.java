package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
	ShortestPath findShortestPath(LineSections lineSections, Station sourceStation, Station targetStation);
}
