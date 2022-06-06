package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.finder.DijkstraPathFinder;
import nextstep.subway.path.finder.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortPath(Long sourceId, Long targetId) {
        List<Line> lines = lineService.findAll();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        return pathFinder.findShortPath(source, target);
    }
}
