package nextstep.subway.domain.path.application;

import nextstep.subway.domain.line.application.LineService;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.domain.PathFinder;
import nextstep.subway.domain.path.dto.PathFinderRequest;
import nextstep.subway.domain.path.dto.PathFinderResponse;
import nextstep.subway.domain.station.application.StationService;
import nextstep.subway.domain.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathFinderResponse findPaths(PathFinderRequest request) {
        final List<Line> lines = lineService.findAll();
        final List<Station> stations = stationService.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        final List<Station> shortestRoute = pathFinder.findShortestRoute(stations, request.getSource(), request.getTarget());
        final int shortestDistance = pathFinder.findShortestDistance(request.getSource(), request.getTarget());

        List<PathFinderResponse.Station> shortestRouteResponse = shortestRoute.stream()
                .map(PathFinderResponse.Station::of)
                .collect(Collectors.toList());

        return new PathFinderResponse(shortestRouteResponse, shortestDistance);
    }
}
