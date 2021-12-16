package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.error.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.FareDiscountPolicy;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
  private final LineRepository lineRepository;
  private final StationRepository stationRepository;
  private final PathFinder pathFinder;

  public PathService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
    this.lineRepository = lineRepository;
    this.stationRepository = stationRepository;
    this.pathFinder = pathFinder;
  }


  public PathResponse findShortestPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
    List<Line> lines = lineRepository.findAll();
    Station sourceStation = findStation(sourceStationId);
    Station targetStation = findStation(targetStationId);
    pathFinder.addGraphPropertiesFromLines(lines);

    int totalDistance = pathFinder.findShortestDistance(sourceStation, targetStation);
    int totalFare = FarePolicy.getTotalFare(totalDistance);

    if (loginMember.isUser()) {
      totalFare = FareDiscountPolicy.getDiscountFare(loginMember.getAge(), totalFare);
    }

    return PathResponse.of(pathFinder.findShortestPath(sourceStation, targetStation),
            totalDistance,
            totalFare);
  }

  private Station findStation(Long id) {
    return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않은 지하철역입니다."));
  }
}
