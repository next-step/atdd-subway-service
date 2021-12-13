package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PathFinder {
  List<Station> findShortestPath(Station sourceStation, Station targetStation);

  int findShortestDistance(Station sourceStation, Station targetStation);

  void addGraphPropertiesFromLines(List<Line> lines);
}
