package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private static PathFinder pathFinder = null;

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        Station start = stationService.findStationById(source);
        Station end = stationService.findStationById(target);

        Path shortestPath = getPathFinder().findShortestPath(start, end);

        Fare fare = loginMember.loggedIn() ?
                shortestPath.calculateFare(loginMember.getAge()) : shortestPath.calculateFare();

        return PathResponse.of(shortestPath, fare);
    }

    public PathFinder getPathFinder() {
        if (pathFinder != null) {
            return pathFinder;
        }
        List<Line> lines = lineRepository.findAll();
        pathFinder = PathFinder.create(lines);
        return pathFinder;
    }
}
