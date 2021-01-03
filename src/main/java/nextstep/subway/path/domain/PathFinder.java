package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathResponse;
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

    private int getWeight(PathStation source, PathStation target) {
        return (int) getGraphPath(source, target).getWeight();
    }

    private List<PathStation> getPath(PathStation source, PathStation target) {
        return getGraphPath(source, target).getVertexList();
    }

    private GraphPath<PathStation, DefaultWeightedEdge> getGraphPath(PathStation source, PathStation target) {
        return new DijkstraShortestPath<>(graph).getPath(source, target);
    }

    public PathResponse ofPathResponse(Station sourceStation, Station targetStation) {
        PathStation source = PathStation.of(sourceStation);
        PathStation target = PathStation.of(targetStation);
        return PathResponse.of(getPath(source, target), getWeight(source, target));
    }
}