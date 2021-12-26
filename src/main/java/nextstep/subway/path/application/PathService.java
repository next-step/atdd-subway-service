package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.FarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.DijkstraShortestPathCalculator;
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

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId, LoginMember loginMember) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        PathFinder pathFinder = new PathFinder(new DijkstraShortestPathCalculator());
        PathResponse pathResponse = pathFinder.findPath(lines, sourceStation, targetStation);
        pathResponse.calculateExtraFare(FarePolicy.calculateDistanceOverFare(pathResponse.getDistance()));
        pathResponse.calculateDiscount(FarePolicy.calculateDiscountAgeFare(loginMember.getAge(), pathResponse.getFare()));
        return pathResponse;
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Station을 조회할 수 없습니다."));
    }
}
