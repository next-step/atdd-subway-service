package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        super(DefaultWeightedEdge.class);
        initializeGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(this);
    }

    private void initializeGraph(List<Line> lines){
        lines.stream()
                .flatMap(line -> line.getUnmodifiableSectionList().stream())
                .forEach(section -> {
                    addVertex(section);
                    addEdge(section);
                });
    }

    private void addEdge(Section section) {
        DefaultWeightedEdge defaultWeightedEdge = this.addEdge(section.getUpStation(), section.getDownStation());
        this.setEdgeWeight(defaultWeightedEdge, section.getDistance().getValue());
    }

    private void addVertex(Section section) {
        this.addVertex(section.getUpStation());
        this.addVertex(section.getDownStation());
    }

    public SubwayShortestPath findPath(Station startStation, Station endStation) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        return new SubwayShortestPath(path.getVertexList(), (int) path.getWeight());
    }
}
