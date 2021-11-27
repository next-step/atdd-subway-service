package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
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

    public PathResponse findShortestPath(PathRequest request) {
        return PathResponse.from(shortestPath(
            station(request.getSource()),
            station(request.getTarget())
        ));
    }

    public Path shortestPath(Station source, Station target) {
        return shortestPathFinder()
            .path(source, target);
    }

    private ShortestPathFinder shortestPathFinder() {
        return ShortestPathFinder.from(lineService.findAll());
    }

    private Station station(long stationId) {
        return stationService.findById(stationId);
    }
}
