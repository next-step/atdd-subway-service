package nextstep.subway.path.application;

import java.util.NoSuchElementException;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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
        Station startStation = stationRepository.findById(startStationId).orElseThrow(NoSuchElementException::new);
        Station endStation = stationRepository.findById(endStationId).orElseThrow(NoSuchElementException::new);
        PathFindResult findResult = null;
        try {
            findResult = pathFindService.findShortestPath(startStation, endStation);
        } catch (NotExistPathException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return PathResponse.of(findResult);
    }
}
