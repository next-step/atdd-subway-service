package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
  List<Station> findShortestPath(Station sourceStation, Station targetStation);

  int findShortestDistance(Station sourceStation, Station targetStation);

  void addGraphPropertiesFromLines(List<Line> lines);
}
