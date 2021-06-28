package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
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

    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(Long start, Long end) {
        Station startStation = stationService.findStationById(start);
        Station endStation = stationService.findStationById(end);
        Lines lines = new Lines(lineRepository.findAll());

        PathFinder pathFinder = new PathFinder(lines);
        Path shortestPath = pathFinder.getDijkstraShortestPath(startStation, endStation);

        return PathResponse.of(shortestPath);
    }
}
