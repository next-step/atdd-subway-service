package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathSection;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<PathStation, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public PathFinder(List<PathSection> sections) {
        sections.forEach(section ->
                this.addGraphVertex(
                        section.getSource(),
                        section.getTarget(),
                        section.getWeight()));
    }

    private void addGraphVertex(PathStation source, PathStation target, double weight) {
        graph.addVertex(source);
        graph.addVertex(target);
        graph.setEdgeWeight(graph.addEdge(source, target), weight);
    }

    public int getWeight(Station source, Station target) {
        return (int) getGraphPath(PathStation.of(source), PathStation.of(target)).getWeight();
    }

    public List<PathStation> getPath(Station source, Station target) {
        return getGraphPath(PathStation.of(source), PathStation.of(target)).getVertexList();
    }

    private GraphPath<PathStation, DefaultWeightedEdge> getGraphPath(PathStation source, PathStation target) {
        return new DijkstraShortestPath<>(graph).getPath(source, target);
    }

}