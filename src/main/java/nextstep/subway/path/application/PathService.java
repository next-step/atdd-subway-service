package nextstep.subway.path.application;

import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.path.fare.policy.FarePolicies;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineQueryService lineQueryService;
    private final FarePolicies farePolicies;

    public PathService(StationService stationService, LineQueryService lineQueryService,
                       FarePolicies farePolicies) {
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
        this.farePolicies = farePolicies;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        PathFinder pathFinder = createPathFinder();

        Path path = pathFinder.find(sourceStation, targetStation);
        Fare fare = farePolicies.calculate(path);

        return new PathResponse(path.getStations(), path.getDistance(), fare);
    }

    private PathFinder createPathFinder() {
        return new PathFinder(lineQueryService.getAllLines());
    }
}
