package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.Set;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {
    private final Set<Line> lines;

    private Lines(Set<Line> lines) {
        this.lines = lines;
    }

    public static Lines valueOf(Set<Line> lines) {
        return new Lines(lines);
    }

    public Set<Line> lines() {
        return lines;
    }

    public GraphPath<Station, DefaultWeightedEdge> shortestPath(Station source, Station target) {
        validateNotSameStation(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(getGraph());
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validatePathNotNull(path);
        return path;
    }

    private void validatePathNotNull(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new NotFoundException("최단경로를 조회할 수 없습니다.");
        }
    }

    private void validateNotSameStation(Station source, Station target) {
        if(source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            line.makeGraph(graph);
        }
        return graph;
    }
}
