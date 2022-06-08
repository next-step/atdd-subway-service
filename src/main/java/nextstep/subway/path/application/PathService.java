package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final PathFindService pathFindService;

    public PathService(StationRepository stationRepository, PathFindService pathFindService) {
        this.stationRepository = stationRepository;
        this.pathFindService = pathFindService;
    }

    public PathResponse findShortestPath(Long startStationId, Long endStationId) {
        Station startStation = stationRepository.findById(startStationId).get();
        Station endStation = stationRepository.findById(endStationId).get();
        PathFindResult findResult = pathFindService.findShortestPath(startStation, endStation);
        return PathResponse.of(findResult);
    }
}
