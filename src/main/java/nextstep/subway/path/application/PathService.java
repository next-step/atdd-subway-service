package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.ShortestPathRequest;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService, LineService lineService, PathFinder pathFinder) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public ShortestPathResponse findShortestPath(ShortestPathRequest requestDto) {
        Station starting = stationService.findStationById(requestDto.getStartingStationsId());
        Station destination = stationService.findStationById(requestDto.getDestinationStationsId());

        pathFinder.findShortestPath(lineService.findAllLines(), starting, destination);

        return new ShortestPathResponse();
    }
}
