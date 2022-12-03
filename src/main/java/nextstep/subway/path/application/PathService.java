package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);
        PathFinder pathFinder = initPathFinder();
        return PathResponse.from(pathFinder.findShortestPath(source, target));
    }

    private PathFinder initPathFinder() {
        return new PathFinder(lineService.findAllLines());
    }
}
