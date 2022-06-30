package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.fare.domain.FarePolicy;
import nextstep.subway.fare.domain.FarePolicyFactory;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationFinder;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final StationFinder stationFinder;
    private final LineRepository lineRepository;

    public PathService(StationFinder stationFinder, LineRepository lineRepository) {
        this.stationFinder = stationFinder;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        Station upStation = stationFinder.findStationById(source);
        Station downStation = stationFinder.findStationById(target);
        Lines lines = new Lines(lineRepository.findAll());

        PathFinder pathFinder = new PathFinder();
        ShortestPath shortestPath = pathFinder.findShortestPath(lines, upStation, downStation);

        FarePolicy farePolicy = FarePolicyFactory.of(loginMember, lines, shortestPath);
        return new PathResponse(shortestPath, farePolicy.fare());
    }
}
