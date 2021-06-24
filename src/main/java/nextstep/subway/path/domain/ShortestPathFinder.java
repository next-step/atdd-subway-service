package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public interface ShortestPathFinder {

  static ShortestPathFinder getDefault(Lines lines) {
    return PathFinder.init(lines);
  }

  Path findShortestPath(Long sourceStationId, Long targetStationId);
}
