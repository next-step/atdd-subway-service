package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.SameEndpointException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse getShortestPath(Long sourceId, Long targetId) {
        checkPathEndpoints(sourceId, targetId);

        List<Line> lines = lineService.findLinesEntities();
        PathFinder pathFinder = new PathFinder(lines);

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Path path = pathFinder.findPath(source, target);
        return PathResponse.of(path);
    }

    private void checkPathEndpoints(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new SameEndpointException("출발지와 목적지가 동일하여 경로를 탐색할 수 없습니다.");
        }
    }
}
