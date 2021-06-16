package nextstep.subway.path.application;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

  private final StationRepository stationRepository;
  private final SectionRepository sectionRepository;

  public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
    this.stationRepository = stationRepository;
    this.sectionRepository = sectionRepository;
  }

  public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
    PathFinder pathFinder = PathFinder.init(stationRepository.findAll(), sectionRepository.findAll());
    return PathResponse.from(pathFinder.findShortestPath(sourceStationId, targetStationId));
  }
}
