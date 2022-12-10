package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId, Integer age) {
        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);
        PathFinder pathFinder = new PathFinder();
        Path shortestPath = pathFinder.findShortestPath(lineService.findAllLines(), source, target);
        return PathResponse.from(shortestPath, shortestPath.getCalculateFare(age));
    }
}
