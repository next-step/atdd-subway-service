package nextstep.subway.line.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathGraph {

    public static GraphPath findPath(Long source, Long target, List<Line> lines){
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertex(graph, lines);
        setEdgeWeight(graph, lines);
        DijkstraShortestPath stationGraph = new DijkstraShortestPath(graph);
        return stationGraph.getPath(source.toString(), target.toString());
    }
    private static void setVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().forEach(
                line -> line.getStations().stream().forEach(
                        station -> graph.addVertex(station.getId().toString())
                )
        );
    }
    private static void setEdgeWeight(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().forEach(
                line -> line.getSections()
                        .stream()
                        .forEach(
                                section ->
                                        graph.setEdgeWeight(
                                                graph.addEdge(section.getUpStation().getId().toString(), section.getDownStation().getId().toString()),
                                                section.getDistance()
                                        )
                        )
        );
    }
}
