package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long source, Long target) {
        return getDijkstraSortestPath(source, target);
    }

    public PathResponse getDijkstraSortestPath(Long source, Long target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(createGraph());
        Station startStation = stationService.findById(source);
        Station endStation = stationService.findById(target);

        List<Station> stations = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
        int distance = (int) dijkstraShortestPath.getPathWeight(startStation, endStation);

        return getPathResponse(stations, distance);
    }

    WeightedMultigraph<Station, DefaultWeightedEdge> createGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Station> stations = stationService.findAll();
        List<Section> sections = lineService.findAllSection();

        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return graph;
    }

    private PathResponse getPathResponse(List<Station> stations, int distance) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance);
    }
}
