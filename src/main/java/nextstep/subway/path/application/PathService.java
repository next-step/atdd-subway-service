package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        List<Line> lines = lineService.findAllLines();
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation);
        return PathResponse.from(path);
    }
}
