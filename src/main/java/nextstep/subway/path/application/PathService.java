package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station upStation = stationService.findStationById(source);
        Station downStation = stationService.findStationById(target);
        Lines lines = new Lines(lineRepository.findAll());

        PathFinder pathFinder = new PathFinder();
        ShortestPath shortestPath = pathFinder.findShortestPath(lines, upStation, downStation);
        return new PathResponse(shortestPath);
    }
}
