package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest request) {
        List<Line> lines = lineService.findLines();
        PathFinder pathFinder = new PathFinder(lines);
        return findShortestPath(request, pathFinder);
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest request, PathFinder pathFinder) {
        Station srcStation = stationService.findById(request.getSrcStationId());
        Station destStation = stationService.findById(request.getDestStationId());

        if (srcStation.equals(destStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        if (!pathFinder.containsStation(srcStation) || !pathFinder.containsStation(destStation)) {
            throw new IllegalArgumentException("출발역이나 도착역이 노선 내에 존재하지 않습니다.");
        }

        if (!pathFinder.stationsConnected(srcStation, destStation)) {
            throw new RuntimeException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
        
        Path path = pathFinder.getShortestPath(srcStation, destStation);
        return PathResponse.of(path);
    }
}
