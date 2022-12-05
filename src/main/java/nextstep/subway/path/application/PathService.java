package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private StationService stationService;
    private LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestRoute(Long sourceStationId, Long targetStationId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        lineService.findLines().stream().forEach(line -> line.makeGraph(graph));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return PathResponse.of(dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList(),
                (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight());
    }
}
