package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long startStationId, Long endStationId) {
        validateStations(startStationId, endStationId);

        Lines lines = new Lines(lineService.findLines());
        DijkstraShortestPath path = lines.createPath();
        PathFinder pathFinder = new PathFinder(path);

        Station startStation = stationService.findById(startStationId);
        Station endStation = stationService.findById(endStationId);

        Path findPath = pathFinder.findPath(startStation, endStation);

        return PathResponse.of(findPath);
    }

    private void validateStations(Long startStationId, Long endStationId) {
        if(isSameStationId(startStationId, endStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private boolean isSameStationId(Long startStationId, Long endStationId) {
        return Objects.equals(startStationId, endStationId);
    }
}
