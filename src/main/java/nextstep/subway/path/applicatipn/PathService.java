package nextstep.subway.path.applicatipn;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DijkstraShortestPathStrategy;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathInterface;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.GetStationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(Long sourceId, Long targetId) {
        GetStationDto station = stationService.findStationById(sourceId, targetId);
        List<Line> lines = lineService.findAll();
        PathInterface strategy = DijkstraShortestPathStrategy.from(lines);
        PathFinder pathFinder = PathFinder.of(strategy, station.getSourceStation(), station.getTargetStation());

        return PathResponse.from(pathFinder);
    }
}
