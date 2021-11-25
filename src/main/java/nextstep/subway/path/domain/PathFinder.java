package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;
    private final Line line;

    public PathFinder(Line line) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.line = line;
    }

    public List<Station> getDijkstraShortestPath(Station source, Station target) {
        validateLineStation(source, target);
        InitializeVertexAndEdge();

        List<Station> result = new ArrayList<>();
        dijkstraShortestPath.getPath(source.getName(), target.getName())
                .getVertexList()
                .forEach(f ->
                        result.addAll(line.getStations()
                                .stream()
                                .filter(it -> f.equals(it.getName()))
                                .collect(Collectors.toList()))
                );

        return result;
    }

    private void InitializeVertexAndEdge() {
        line.getStations().forEach(it -> graph.addVertex(it.getName()));
        line.getSections().forEach(it ->
                graph.setEdgeWeight(graph.addEdge(it.getUpStation().getName(), it.getDownStation().getName()), it.getIntegerDistance())
        );
    }

    private void validateLineStation(Station source, Station target) {
        boolean isExistSource = line.getStations()
                .stream()
                .anyMatch(it -> it == source);
        boolean isExistTarget = line.getStations()
                .stream()
                .anyMatch(it -> it == target);

        if (!isExistSource || !isExistTarget) {
            throw new SectionException(ErrorCode.NOT_FOUND_ENTITY, "검색 역이 잘못되었습니다.");
        }
        if (source == target) {
            throw new SectionException(ErrorCode.BAD_ARGUMENT, "출발역과 도착역이 동일합니다.");
        }
    }

    public int getSumLineStationsDistance(Station source, Station target) {
        List<Station> dijkstra = getDijkstraShortestPath(source, target);

        return line.getSections()
                .stream()
                .filter(it ->
                        (dijkstra.contains(it.getUpStation()) && dijkstra.containsAll(Arrays.asList(it.getUpStation(), it.getDownStation())))
                )
                .distinct()
                .mapToInt(Section::getIntegerDistance)
                .sum();
    }

}
