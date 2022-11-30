package nextstep.subway.path.application;

import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathService(StationService stationService, LineQueryService lineQueryService) {
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        PathFinder pathFinder = getPathFinder();

        return pathFinder.find(sourceStation, targetStation);
    }

    private PathFinder getPathFinder() {
        return new PathFinder(lineQueryService.getAllLines());
    }
}
