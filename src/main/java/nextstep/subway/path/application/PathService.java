package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathMap;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final PathFinder pathFinder;
    private final PathMap pathMap;
    private final StationService stationService;
    private final LineService lineService;

    @Autowired
    public PathService(PathFinder pathFinder, PathMap pathMap, StationService stationService, LineService lineService) {
        this.pathFinder = pathFinder;
        this.pathMap = pathMap;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        WeightedMultigraph<Station, DefaultWeightedEdge> map = pathMap.createMap(lineService.findAllLines());

        return pathFinder.findShortestPath(map, source, target);
    }
}
