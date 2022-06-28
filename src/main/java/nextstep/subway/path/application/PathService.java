package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
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

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station source = stationService.findStationById(pathRequest.getSourceId());
        Station target = stationService.findStationById(pathRequest.getTargetId());

        PathFinder pathFinder = PathFinder.of(lineRepository.findAll());
        return PathResponse.of(pathFinder.getShortestPath(source, target));
    }
}
