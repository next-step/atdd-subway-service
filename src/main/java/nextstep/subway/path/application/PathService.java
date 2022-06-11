package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId, LoginMember loginMember) {
        requireNonNull(sourceStationId, "sourceStationId");
        requireNonNull(targetStationId, "targetStationId");
        PathFinder pathFinder = createPathFinder();
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        Path shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);
        return new PathResponse(shortestPath, shortestPath.calculateFare(loginMember));
    }

    private PathFinder createPathFinder() {
        List<Line> allLines = lineService.findAllLines();
        return new PathFinder(allLines);
    }
}
