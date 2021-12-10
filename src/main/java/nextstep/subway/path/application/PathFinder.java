package nextstep.subway.path.application;

import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.route.PathRoute;
import nextstep.subway.station.domain.Station;

public interface PathFinder {

	PathRoute findShortestPath(List<Line> lines, Station departStation, Station arriveStation);
}