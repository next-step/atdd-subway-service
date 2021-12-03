package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public interface PathFinder {

	Path findShortestPath(List<Sections> linesSections, Station departStation, Station arriveStation);
}
