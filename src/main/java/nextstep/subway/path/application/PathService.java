package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.AgeFarePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final StationGraph graph;

    public PathService(LineService lineService, StationService stationService,
        StationGraph graph) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.graph = graph;
    }

    public PathResponse getShortestPaths(LoginMember loginMember, Long source, Long target) {
        Path shortestPath = getShortestPath(source, target);
        if (loginMember.isEmpty()) {
            return PathResponse.of(shortestPath);
        }
        return PathResponse.of(shortestPath.applyAgePolicy(AgeFarePolicy.findByAge(loginMember.getAge())));
    }

    private Path getShortestPath(Long source, Long target) {
        Station sourceStation = stationService.findStation(source);
        Station targetStation = stationService.findStation(target);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = PathFinder.create(graph, lines);
        return pathFinder.findShortestPath(sourceStation, targetStation);
    }

}
