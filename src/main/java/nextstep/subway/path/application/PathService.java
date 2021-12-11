package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(final Long sourceStationId, final Long targetStationId) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.of(lines);
        Station upStation = stationService.findStationById(sourceStationId);
        Station downStation = stationService.findStationById(targetStationId);
        return pathFinder.findShortestPath(upStation, downStation);
    }
}
