package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(PathRequest request) {
        PathFinder pathFinder = new PathFinder(lineService.findAll());
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());
        pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.of(pathFinder.getStationShortestPath(), pathFinder.getDistanceShortestPath());
    }
}
