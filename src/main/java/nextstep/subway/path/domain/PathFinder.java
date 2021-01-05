package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
@Component
public class PathFinder {

    public Path findPath(Lines lines, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = initGraph(lines);
        return getPath(graph, sourceStation, targetStation);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.getStations().forEach(graph::addVertex);
        lines.getSections()
                .forEach(it -> {
                    DefaultWeightedEdge edge = graph.addEdge(it.getUpStation(), it.getDownStation());
                    graph.setEdgeWeight(edge, it.getDistance());
                });
        return graph;
    }

    private Path getPath(
            WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            Station sourceStation, Station targetStation) {

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return Path.of(path);
    }

}
