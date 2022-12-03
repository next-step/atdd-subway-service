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
        DijkstraShortestPath stationGraph = getStationGraph(graph, sections);
        GraphPath path = stationGraph.getPath(source, target);

        return new PathResponse(createStations(path), (int)path.getWeight());
    }

    private DijkstraShortestPath getStationGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        setVertex(graph, sections);
        setEdgeWeight(graph, sections);
        return new DijkstraShortestPath(graph);
    }

    private void setVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.stream().forEach(
                section -> addVertex(graph, section)
        );
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section){
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
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
