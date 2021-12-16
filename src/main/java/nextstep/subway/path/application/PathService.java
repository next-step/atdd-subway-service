package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
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

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());

        Lines lines = lineService.findLines();
        PathFinder pathFinder = PathFinder.from(lines);
        Path path = pathFinder.findShortestPath(lines, sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
