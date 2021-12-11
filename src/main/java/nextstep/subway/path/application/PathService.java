package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    
    private final LineService lineService;
    private final StationService stationService;
    
    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        
        PathFinder pathFinder = PathFinder.of(lineService.findLines());
        List<Station> stations = pathFinder.findShortestPath(sourceStation, targetStation);
        int distance = pathFinder.findShortestDistance(sourceStation, targetStation);
        
        return PathResponse.of(stations, distance);
    }

}
