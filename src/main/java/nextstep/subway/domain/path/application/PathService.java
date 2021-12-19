package nextstep.subway.domain.path.application;

import nextstep.subway.domain.auth.domain.AnonymousUser;
import nextstep.subway.domain.auth.domain.User;
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
    public PathFinderResponse findPaths(PathFinderRequest request, User user) {
        final List<Line> lines = lineService.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(request.getSource(), request.getTarget());

        final Fare fare = getFare(user, lines, shortestRoute);

        return PathFinderResponse.of(shortestRoute, fare);
    }

    private Fare getFare(final User user, final List<Line> lines, final Route shortestRoute) {
        if (isNonLoginUser(user)) {
            return new Fare(shortestRoute.getDistance(), lines);
        }
        return new Fare(shortestRoute.getDistance(), lines, user);
    }

    private boolean isNonLoginUser(final User user) {
        return user instanceof AnonymousUser;
    }
}
