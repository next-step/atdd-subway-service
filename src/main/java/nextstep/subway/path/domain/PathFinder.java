package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public PathFinder(List<Section> sections) {
        sections.forEach(section ->
                this.addGraphVertex(
                        section.getUpStation(),
                        section.getDownStation(),
                        section.getDistance()));
    }

    private void addGraphVertex(Station source, Station target, double weight) {
        graph.addVertex(source);
        graph.addVertex(target);
        graph.setEdgeWeight(graph.addEdge(source, target), weight);
    }

    public int getWeight(Station source, Station target) {
        return (int) getGraphPath(source, target).getWeight();
    }

    public List<Station> getPath(Station source, Station target) {
        return getGraphPath(source, target).getVertexList();
    }

    private GraphPath<Station, DefaultWeightedEdge> getGraphPath(Station source, Station target) {
        return new DijkstraShortestPath<>(graph).getPath(source, target);
    }

}