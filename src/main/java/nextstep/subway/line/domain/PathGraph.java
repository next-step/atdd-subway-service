package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathGraph {

    public PathResponse findPath(Station source, Station target, List<Section> sections){
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath stationGraph = getStationGraph(graph, sections, source);
        GraphPath path = stationGraph.getPath(source, target);

        return new PathResponse(createStations(path), (int)path.getWeight());
    }

    private DijkstraShortestPath getStationGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections, Station source) {
        setVertex(graph, sections, source);
        setEdgeWeight(graph, sections);
        return new DijkstraShortestPath(graph);
    }

    private void setVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections, Station source) {
        graph.addVertex(source);
        sections.stream().forEach(
                section -> graph.addVertex(section.getDownStation())
        );
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.stream().forEach(
                section -> addEdgeWeight(graph, section)
        );
    }

    private void addEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
        graph.setEdgeWeight(edge, section.getDistance());
    }

    private List<StationResponse> createStations(GraphPath path) {
        List<Station> vertexList = path.getVertexList();
        return vertexList.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
