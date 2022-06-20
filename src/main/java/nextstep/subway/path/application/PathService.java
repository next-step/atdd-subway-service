package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.DiscountByAge;
import nextstep.subway.fare.domain.FareByDistance;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Station sourceStation = stationService.findStationById(pathRequest.getSourceStationId());
        Station targetStation = stationService.findStationById(pathRequest.getTargetStationId());
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());
        ShortestPath path = pathFinder.getShortestPath(sourceStation, targetStation);
        return PathResponse.of(path.getShortestStations(), path.getShortestDistance(), path.getFare());
    }
}
