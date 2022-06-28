package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.application.FareService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final FareService fareService;

    public PathService(LineService lineService, StationService stationService, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareService = fareService;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findById(sourceStationId);
        Station targetStation = stationService.findById(targetStationId);
        PathFinder pathFinder = new DijkstraPathFinder(lineService.findAllLines());

        ShortestPath shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);
        long fare = fareService.calculateFare(loginMember.getAgeGrade(), shortestPath);

        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), fare);
    }
}
