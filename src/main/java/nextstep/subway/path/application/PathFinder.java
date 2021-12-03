package nextstep.subway.path.application;

import java.util.List;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public interface PathFinder {

	Path findShortestPath(List<Sections> linesSections, Station departStation, Station arriveStation);
}
