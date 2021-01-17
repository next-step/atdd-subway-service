package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.domain.graph.DijkstraPath;
import nextstep.subway.path.domain.graph.PathAlgorithm;
import nextstep.subway.path.domain.graph.StationGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        PathFinder pathFinder = makePathFinder();

        Path shortestPath = pathFinder.findShortestPath(source, target);
        return PathResponse.of(shortestPath.discountByAge(loginMember.getAge()));
    }

    private PathFinder makePathFinder() {
        StationGraph stationGraph = new StationGraph(lineService.findLines());
        PathAlgorithm pathAlgorithm = new DijkstraPath(stationGraph.generateGraph());
        return new PathFinder(stationGraph, pathAlgorithm);
    }
}
