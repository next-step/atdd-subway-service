package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long startStationId, Long endStationId) {
        if(isSameStationId(startStationId, endStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);

        Station startStation = stationService.findById(startStationId);
        Station endStation = stationService.findById(endStationId);

        Path findPath = pathFinder.findPath(startStation, endStation);

        return PathResponse.of(findPath);
    }

    private boolean isSameStationId(Long startStationId, Long endStationId) {
        return Objects.equals(startStationId, endStationId);
    }
}
