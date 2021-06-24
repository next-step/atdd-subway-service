package nextstep.subway.path.application;

import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathService(StationService stationService, LineQueryService lineQueryService) {
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Line> lines = lineQueryService.findLines();
        Station startStation = stationService.findById(pathRequest.getSource());
        Station endStation = stationService.findById(pathRequest.getTarget());
        PathFinder pathFinder = new PathFinder(lines);
        return PathResponse.of(pathFinder.findPath(startStation, endStation));
    }
}
