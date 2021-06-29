package nextstep.subway.path.application;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Station source = stationRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."));
        Station target = stationRepository.findById(targetId).orElseThrow(() -> new IllegalArgumentException("도착역이 존재하지 않습니다."));
        List<StationResponse> shortestPaths = pathFinder.findPaths(source, target);
        return new PathResponse(shortestPaths, 0);
    }
}
