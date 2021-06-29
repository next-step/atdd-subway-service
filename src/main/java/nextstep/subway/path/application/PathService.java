package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
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

    public PathResponse getShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        List<Line> lines = lineService.findLinesEntities();
        PathFinder pathFinder = new PathFinder(lines);

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Path path = pathFinder.findPath(source, target);
        return PathResponse.of(path, path.totalFareOf(loginMember));
    }
}
