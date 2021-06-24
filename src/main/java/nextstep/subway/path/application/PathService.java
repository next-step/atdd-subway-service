package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

  private final LineRepository lineRepository;
  private final StationRepository stationRepository;

  public PathService(LineRepository lineRepository, StationRepository stationRepository) {
    this.lineRepository = lineRepository;
    this.stationRepository = stationRepository;
  }

  public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
    ShortestPathFinder pathFinder = ShortestPathFinder.getDefault(new Lines(lineRepository.findAll()));
    return PathResponse.from(pathFinder.findShortestPath(findStation(sourceStationId), findStation(targetStationId)));
  }

  public PathResponse findShortestPathForLoginMember(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
    ShortestPathFinder pathFinder = ShortestPathFinder.getDefault(new Lines(lineRepository.findAll()));
    return PathResponse.of(loginMember.buildAgeDiscount(), pathFinder.findShortestPath(findStation(sourceStationId), findStation(targetStationId)));
  }

  private Station findStation(Long stationId) {
    return stationRepository.findById(stationId)
        .orElseThrow(StationNotExistException::new);
  }
}
