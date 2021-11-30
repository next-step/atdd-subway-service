package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.FareDiscountPolicy;
import nextstep.subway.path.domain.FareDistancePolicy;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService,
        StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(LoginMember member, PathRequest request) {
        Path path = shortestPath(
            station(request.getSource()),
            station(request.getTarget())
        );
        return PathResponse.of(path, farePolicy(member, path).fare());
    }

    public Path shortestPath(Station source, Station target) {
        return shortestPathFinder()
            .path(source, target);
    }

    public boolean isInvalidPath(Station source, Station target) {
        return shortestPathFinder()
            .isInvalidPath(source, target);
    }

    private FarePolicy farePolicy(LoginMember member, Path path) {
        return FarePolicy.of(
            FareDistancePolicy.from(path.distance()),
            FareDiscountPolicy.from(member),
            path.sections());
    }

    private ShortestPathFinder shortestPathFinder() {
        return ShortestPathFinder.from(lineService.findAll());
    }

    private Station station(long stationId) {
        return stationService.findById(stationId);
    }
}
