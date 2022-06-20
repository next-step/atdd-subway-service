package nextstep.subway.path.application;

import nextstep.subway.fare.domain.DistanceExtraFare;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService, PathFinder pathFinder) {
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse getPath(Long sourceId, Long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Path path = pathFinder.getPath(source, target);
        Fare fare = Fare.of(DistanceExtraFare.BASE_FARE);
        fare = fare.addExtraOf(path.getDistance());
        return PathResponse.of(path.getStations(), path.getDistance(), fare);
    }
}
