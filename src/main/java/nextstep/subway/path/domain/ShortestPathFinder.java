package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface ShortestPathFinder {

  static ShortestPathFinder getDefault(List<Station> wholeStations, List<Section> wholeSections) {
    return PathFinder.init(wholeStations, wholeSections);
  }

  Path findShortestPath(Long sourceStationId, Long targetStationId);
}
