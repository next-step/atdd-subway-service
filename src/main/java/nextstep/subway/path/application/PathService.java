package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
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
    public PathResponse findShortestPath(Long sourceId, Long targetId, int age) {
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        
        PathFinder pathFinder = PathFinder.of(Lines.of(lineService.findLines()));
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);
        Fare fare = Fare.of(path.getDistance(), path.getSurcharge(), age);
        
        return PathResponse.of(path, fare);
    }

}
