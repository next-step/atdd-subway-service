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
import java.util.Optional;

public class PathFinder {
    private final WeightedMultigraph<PathStation, DefaultWeightedEdge> graph;

    public PathFinder(List<PathSection> sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section ->
                this.addGraphVertex(
                        section.getSource(),
                        section.getTarget(),
                        section.getWeight()));
    }

    private void addGraphVertex(PathStation source, PathStation target, double weight) {
        this.graph.addVertex(source);
        this.graph.addVertex(target);
        this.graph.setEdgeWeight(this.graph.addEdge(source, target), weight);
    }

    private int getWeight(PathStation source, PathStation target) {
        return (int) getGraphPath(source, target).getWeight();
    }

    private List<PathStation> getPath(PathStation source, PathStation target) {
        return getGraphPath(source, target).getVertexList();
    }

    private GraphPath<PathStation, DefaultWeightedEdge> getGraphPath(PathStation source, PathStation target) {
        return Optional.ofNullable(new DijkstraShortestPath<>(this.graph)
                .getPath(source, target))
                .orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결 되어 있지 않습니다."));
    }

    public PathResponse ofPathResponse(Station sourceStation, Station targetStation) {
        PathStation source = PathStation.of(sourceStation);
        PathStation target = PathStation.of(targetStation);
        return PathResponse.of(getPath(source, target), getWeight(source, target));
    }
}