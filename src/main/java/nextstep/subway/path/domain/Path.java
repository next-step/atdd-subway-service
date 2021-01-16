package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Path {
    private final WeightedMultigraph<Station, PathEdge> graph;

    private Path(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(PathEdge.class);
        initGraph(lines);
    }

    public static Path of(List<Line> lines) {
        return new Path(lines);
    }

    private void initGraph(List<Line> lines) {
        for (Line line : lines) {
            addLineToGraph(line);
        }
    }

    private void addLineToGraph(Line line) {
        line.getSections().forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            PathEdge pathEdge = PathEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), pathEdge);
        });
    }

    public ShortestPath findShortestPath(Station source, Station target) {
        validateSection(source, target);
        try {
            GraphPath<Station, PathEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
            return ShortestPath.of(path.getVertexList(), path.getEdgeList(), (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("경로에 포함되어 있지 않은 역입니다.");
        }
    }

    private void validateSection(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("조회하려는 출발지와 도착지가 같습니다.");
        }
    }
}
