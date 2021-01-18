package nextstep.subway.line.domain;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private static final int CRITERION_OF_DISTANCE = 8;
    private final DijkstraShortestPath<Station, CustomDefaultWeightedEdge> dijkstraShortestPath;
    private final List<Line> lines;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, CustomDefaultWeightedEdge> graph = new WeightedMultigraph<>(CustomDefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertex(graph, line);
            applyEdgeWeight(graph, line);
        }
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.lines = lines;
    }

    private void addVertex(WeightedMultigraph<Station, CustomDefaultWeightedEdge> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void applyEdgeWeight(WeightedMultigraph<Station, CustomDefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public PathResponse findPath(CostType costType, Station source, Station target) {
        List<Station> result = emptyList();
        int weight = 0;
        int additionalCost = 0;
        try {
            result = dijkstraShortestPath.getPath(source, target).getVertexList();
            weight = (int) dijkstraShortestPath.getPath(source, target).getWeight();
            checkSourceAndTarget(result.size());
            additionalCost = getAdditionalCostOfLine(dijkstraShortestPath.getPath(source, target).getEdgeList());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("노선에 존재하지 않는 역으로 경로를 찾을 수 없습니다");
        } catch (NullPointerException e) {
            throw new IllegalStateException("도달할 수 없는 경로를 조회하셨습니다");
        }
        return PathResponse.of(result, weight, costType.getFare(weight) + additionalCost);
    }

    private void checkSourceAndTarget(int size) {
        if (size == 1) {
            throw new RuntimeException("출발역과 도착역이 동일합니다");
        }
    }

    private int getAdditionalCostOfLine(List<CustomDefaultWeightedEdge> edges) {
        List<LinkedList<Station>> edgeStations = getEdgeStations(edges);
        if (edgeStations.isEmpty()) {
            return 0;
        }
        return lines.stream()
                .filter(line -> line.containsEdge(edgeStations))
                .map(Line::getAdditionalCost)
                .max(Comparator.comparingInt(AdditionalCost::getCost))
                .get().getCost();
    }

    private List<LinkedList<Station>> getEdgeStations(List<CustomDefaultWeightedEdge> edges) {
        return edges.stream()
                .filter(e -> e.getWeight() > CRITERION_OF_DISTANCE)
                .map(e -> new LinkedList<>(asList(e.getSource(), e.getTarget())))
                .collect(toList());
    }
}
