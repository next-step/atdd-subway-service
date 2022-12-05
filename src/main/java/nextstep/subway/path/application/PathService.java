package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private StationService stationService;
    private LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestRoute(Long sourceStationId, Long targetStationId) {
        PathFinder pathFinder = new PathFinder(stationService.findStationById(sourceStationId),
                stationService.findStationById(targetStationId), lineService.getSectionDistanceGraph());
        return PathResponse.of(pathFinder.getShortestPathStationList(), pathFinder.getShortestPathDistance());
    }
}
