package nextstep.subway.path.application;

import java.util.Map;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationService stationService;
    private PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationService stationService, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Path path = findPath(pathRequest.getSource(), pathRequest.getTarget());
        Fare fare = new FareCalculator(loginMember).calculate(path);
        return new PathResponse(path.getStations(), path.getDistance(), fare);
    }

    private Path findPath(Long source, Long target) {
        Lines lines = new Lines(lineRepository.findAll());
        Map<Long, Station> stations = stationService.findStationsByIds(source, target);
        Station sourceStation = stations.get(source);
        Station targetStation = stations.get(target);
        return pathFinder.findPath(lines, sourceStation, targetStation);
    }

}
