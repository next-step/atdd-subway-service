package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService, LineService lineService, PathFinder pathFinder) {
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.lineService = lineService;
    }

    public PathResponse get(long sourceStationId, long targetStationId) {
        Station source = stationService.findStationById(sourceStationId);
        Station target = stationService.findStationById(targetStationId);
        return pathFinder.getShortestPath(lineService.findLines(), source, target);
    }
}
