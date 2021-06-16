package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DijkstraShortestDistance implements ShortestDistance {
    @Override
    public Distance shortestDistance(List<Section> sections, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestGraph(sections, source, target);

        return new Distance(path.getWeight());
    }

    @Override
    public Stations shortestRoute(List<Section> sections, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestGraph(sections, source, target);

        return new Stations(path.getVertexList());
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestGraph(List<Section> sections, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        sections.forEach(item -> {
                    graph.addVertex(item.getUpStation());
                    graph.addVertex(item.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(item.getUpStation(), item.getDownStation()), item.getDistance().toInt());
                });

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        return path;
    }

}
