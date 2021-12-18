package nextstep.subway.domain.path.application;

import nextstep.subway.domain.line.application.LineService;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.domain.Fare;
import nextstep.subway.domain.path.domain.PathFinder;
import nextstep.subway.domain.path.domain.Route;
import nextstep.subway.domain.path.dto.PathFinderRequest;
import nextstep.subway.domain.path.dto.PathFinderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private final LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathFinderResponse findPaths(PathFinderRequest request) {
        final List<Line> lines = lineService.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(request.getSource(), request.getTarget());
        final Fare fare = new Fare(shortestRoute.getDistance(), lines);

        return PathFinderResponse.of(shortestRoute, fare);
    }
}
