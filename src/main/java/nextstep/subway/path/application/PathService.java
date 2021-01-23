package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(PathRequest request, int age) {
        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        pathFinder.findShortestPath(source, target);
        return PathResponse.of(pathFinder.getShortestPath(), age);
    }
}
