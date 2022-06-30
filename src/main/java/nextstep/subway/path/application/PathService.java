package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(LoginMember loginMember, PathRequest request) {
        Station source = stationService.findStationById(request.getSourceId());
        Station target = stationService.findStationById(request.getTargetId());
        List<Line> lines = lineService.getAllLines();

        Path path = pathFinder.findPath(lines, source, target);
        Fare totalFare = fareCalculator.calculate(loginMember, path.getSectionEdges(), path.getDistance());

        return PathResponse.of(path, totalFare.getFare());
    }
}
