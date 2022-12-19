package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(
        LineService lineService,
        StationService stationService,
        PathFinder pathFinder
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        final List<Line> lines = lineService.findLinesAsDomainEntity();
        final Station sourceStation = stationService.findStationByIdAsDomainEntity(source);
        final Station targetStation = stationService.findStationByIdAsDomainEntity(target);
        final ShortestPath shortestPath = pathFinder.findShortestPath(lines, sourceStation, targetStation);
        return PathResponse.from(shortestPath);
    }
}
