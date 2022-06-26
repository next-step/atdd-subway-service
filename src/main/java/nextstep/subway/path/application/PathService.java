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

    public ShortestPathResponse findShortestPath(Long source, Long target) {
        Station starting = stationService.findStationById(source);
        Station destination = stationService.findStationById(target);

        return PathFinder.findShortestPath(lineService.findAllLines(), starting, destination);
    }
}
