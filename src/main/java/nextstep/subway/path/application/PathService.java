package nextstep.subway.path.application;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.AgeType;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(AuthMember authMember, Long sourceId, Long targetId) {
        List<Line> allLines = lineService.findAll();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        PathFinder pathFinder = new DijkstraShortestPathFinder(allLines);
        Path path = pathFinder.findShortestPath(source, target);
        Fare fare = path.calculateFare(authMember);
        return PathResponse.of(path, fare);
    }
}