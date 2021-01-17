package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.*;
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
    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        StationGraph stationGraph = new StationGraph(lineService.findLines());
        PathAlgorithm pathAlgorithm = new DijkstraPath(stationGraph.generateGraph());
        PathFinder pathFinder = new PathFinder(stationGraph, pathAlgorithm);

        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        return PathResponse.of(pathFinder.findShortestPath(source, target));
    }
}
