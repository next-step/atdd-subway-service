package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

  private final LineRepository lineRepository;

  public PathService(LineRepository lineRepository) {
    this.lineRepository = lineRepository;
  }

  public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
    ShortestPathFinder pathFinder = ShortestPathFinder.getDefault(new Lines(lineRepository.findAll()));
    return PathResponse.from(pathFinder.findShortestPath(sourceStationId, targetStationId));
  }
}
