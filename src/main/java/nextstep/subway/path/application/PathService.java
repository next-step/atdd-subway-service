package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public ShortestPathResponse findShortestPath(Long startingStationId, Long destinationStationId) {
        Station starting = stationService.findStationById(startingStationId);
        Station destination = stationService.findStationById(destinationStationId);

        return PathFinder.findShortestPath(lineService.findAllLines(), starting, destination);
    }
}
