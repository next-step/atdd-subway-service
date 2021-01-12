package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import static java.util.Collections.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertex(graph, line);
            applyEdgeWeight(graph, line);
        }
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void applyEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> findPath(Station source, Station target) {
        List<Station> result = emptyList();
        try {
            result = dijkstraShortestPath.getPath(source, target).getVertexList();
            checkSourceAndTarget(result.size());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("노선에 존재하지 않는 역으로 경로를 찾을 수 없습니다");
        } catch (NullPointerException e) {
            throw new IllegalStateException("도달할 수 없는 경로를 조회하셨습니다");
        }
        return result;
    }

    private void checkSourceAndTarget(int size) {
        if (size == 1) {
            throw new RuntimeException("출발역과 도착역이 동일합니다");
        }
    }
}
