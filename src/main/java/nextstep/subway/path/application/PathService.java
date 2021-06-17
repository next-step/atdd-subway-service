package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {

        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        PathFinder pathFinder = new PathFinder(source, target, lineService.findLines());
        return PathResponse.of(pathFinder.findShortestPath());
    }
}
