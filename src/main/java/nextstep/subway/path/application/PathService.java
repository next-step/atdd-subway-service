package nextstep.subway.path.application;

import nextstep.subway.auth.domain.IncompleteLoginMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationQueryService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationQueryService stationQueryService;
    private final LineQueryService lineQueryService;

    public PathService(StationQueryService stationQueryService, LineQueryService lineQueryService) {
        this.stationQueryService = stationQueryService;
        this.lineQueryService = lineQueryService;
    }

    public PathResponse findPath(IncompleteLoginMember incompleteLoginMember, PathRequest pathRequest) {
        List<Line> lines = lineQueryService.findLines();
        Station startStation = stationQueryService.findStationById(pathRequest.getSource());
        Station endStation = stationQueryService.findStationById(pathRequest.getTarget());
        PathFinder pathFinder = new PathFinder(lines);
        return PathResponse.of(pathFinder.findPath(startStation, endStation, incompleteLoginMember));
    }
}
