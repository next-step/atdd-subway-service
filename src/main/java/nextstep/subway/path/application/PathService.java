package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.OverFareCalculator;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final PathFindService pathFindService;
    private SubwayGraphProvider subwayGraphProvider;

    public PathService(StationService stationService, LineService lineService,
                       PathFindService pathFindService,
                       SubwayGraphProvider subwayGraphProvider) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFindService = pathFindService;
        this.subwayGraphProvider = subwayGraphProvider;
    }

    public PathResponse findShortestPath(Long startStationId, Long endStationId) {
        Station startStation = stationService.findStationById(startStationId);
        Station endStation = stationService.findStationById(endStationId);
        PathFindResult findResult = null;
        List<Line> lines = lineService.findAll();
        WeightedMultigraph<Station, SectionEdge> graph = subwayGraphProvider.getSubwayGraph(lines);
        try {
            findResult = pathFindService.findShortestPath(graph, startStation, endStation);
        } catch (NotExistPathException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        int overFareByDistance = OverFareCalculator.calculateOverFareByDistance(findResult);
        findResult.applyOverFare(overFareByDistance);

        return PathResponse.of(findResult);
    }
}
