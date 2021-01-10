package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathFinder {

    private final LineRepository lineRepository;

    private final StationService stationService;

    public PathFinder(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = buildGraph(lines);

        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());

        GraphPath shortestPath = getShortestPath(graph, sourceStation, targetStation);

        List<Station> stations = shortestPath.getVertexList();
        return new PathResponse(stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList()), (int) shortestPath.getWeight());
    }

    private GraphPath getShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return shortestPath;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            buildGraphVertex(graph, line);
            buildGraphEdges(graph, line);
        }
        return graph;
    }

    private void buildGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void buildGraphEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            int distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), distance);
        }
    }
}
