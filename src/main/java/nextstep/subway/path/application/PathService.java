package nextstep.subway.path.application;

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

    public PathService(StationService stationService){
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long start, Long end) {
        Station startStation = stationService.findStationById(start);
        Station endStation = stationService.findStationById(end);

        PathFinder pathFinder = new PathFinder();
        Path shortestPath = pathFinder.getDijkstraShortestPath(startStation, endStation);

        return new PathResponse();
    }
}
