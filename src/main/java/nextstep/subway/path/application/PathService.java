package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        PathFinder pathFinder = new PathFinder(lineService.findAll());
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        GraphPath<Station, DefaultWeightedEdge> graphPath = pathFinder.findPath(source, target);
        return PathResponse.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }
}
