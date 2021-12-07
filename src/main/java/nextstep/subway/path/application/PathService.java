package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPaths(Long source, Long target) {
        Station sourceStation = stationService.findStation(source);
        Station targetStation = stationService.findStation(target);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = PathFinder.of(lines);
        Path shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.of(shortestPath);
    }

}
