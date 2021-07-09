package nextstep.subway.path.application;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.util.FareCalculator;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.path.dto.PathResponse;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target, LoginMember loginMember) {
        validateSameStations(source, target);
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        Lines lines = new Lines(lineRepository.findByStationIdIn(Arrays.asList(source, target)));
        if (PathFinder.of(lines).isConnectedPath(sourceStation, targetStation)) {
            return findPathInSomeLines(loginMember, sourceStation, targetStation, lines);
        }
        return findPathInAllLines(loginMember, sourceStation, targetStation);
    }

    private PathResponse findPathInSomeLines(LoginMember loginMember, Station sourceStation, Station targetStation, Lines lines) {
        Path path = PathFinder.of(lines).findPath(sourceStation, targetStation);
        return PathResponse.of(path, FareCalculator.of(new Distance(path.getTotalDistance()),
                lines.getFinalSurcharge(path), loginMember));
    }

    private PathResponse findPathInAllLines(LoginMember loginMember, Station sourceStation, Station targetStation) {
        Lines lines = new Lines(lineRepository.findAll());
        Path path = PathFinder.of(lines).findPath(sourceStation, targetStation);
        return PathResponse.of(path, FareCalculator.of(new Distance(path.getTotalDistance()),
                lines.getFinalSurcharge(path), loginMember));
    }

    private void validateSameStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalStateException("경로조회 출발역과 도착역이 같습니다.");
        }
    }
}
