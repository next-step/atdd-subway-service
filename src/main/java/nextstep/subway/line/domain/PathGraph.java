package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathGraph {

    private final StationRepository stationRepository;

    public PathGraph(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target, List<Line> lines){
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath stationGraph = getStationGraph(lines, graph);
        GraphPath path = stationGraph.getPath(source.toString(), target.toString());

        return new PathResponse(createStations(path), (int)path.getWeight());
    }

    private DijkstraShortestPath getStationGraph(List<Line> lines, WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        setVertex(graph, lines);
        setEdgeWeight(graph, lines);
        return new DijkstraShortestPath(graph);
    }

    private void setVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().forEach(
                line -> addVertex(graph, line)
        );
    }

    private void addVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        line.getStations().stream().forEach(
                station -> graph.addVertex(station.getId().toString())
        );
    }

    private void setEdgeWeight(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().forEach(
                line -> addEdgeWeight(graph, line)
        );
    }

    private void addEdgeWeight(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        line.getSections().stream().forEach(
                section -> graph.setEdgeWeight(
                                graph.addEdge(
                                        section.getUpStation().getId().toString(),
                                        section.getDownStation().getId().toString()
                                ), section.getDistance()
                        )
        );
    }

    private List<StationResponse> createStations(GraphPath path) {
        List<String> vertexList = path.getVertexList();
        return vertexList.stream()
                .map(vertex -> StationResponse.of(findStation(vertex)))
                .collect(Collectors.toList());
    }

    private Station findStation(String vertex) {
        return stationRepository.findById(Long.valueOf(vertex))
                .orElseThrow(
                        () -> new IllegalArgumentException("역 조회 실패")
                );
    }
}
