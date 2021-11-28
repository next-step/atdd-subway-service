package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.finder.DijkstraShortestPathAlgorithm;
import nextstep.subway.path.finder.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(lines));

        return pathFinder.findShortestPath(sourceStation, targetStation);
    }
}
