package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(
        LineRepository lineRepository,
        StationService stationService
    ) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public ShortestPathResponse getShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> allLines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder();
        return pathFinder.findShortestPath(allLines, sourceStation, targetStation);
    }
}
