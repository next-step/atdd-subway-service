package nextstep.subway.path.application;

import java.util.Map;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        ShortestPath shortestPath = findShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        Fare fare = new FareCalculator(loginMember).calculate(shortestPath);
        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), fare);
    }

    private ShortestPath findShortestPath(Long source, Long target) {
        Lines lines = new Lines(lineRepository.findAll());
        Map<Long, Station> stations = stationService.findStationsByIds(source, target);
        Station sourceStation = stations.get(source);
        Station targetStation = stations.get(target);
        return new PathFinder(lines).findPath(sourceStation, targetStation);
    }

}
