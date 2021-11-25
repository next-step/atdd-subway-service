package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;
    private final List<Section> sections;

    public PathFinder(List<Section> sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.sections = sections;
    }

    public List<Station> getDijkstraShortestPath(Station source, Station target) {
        validateLineStation(source, target);
        InitializeVertexAndEdge();

        List<Station> result = new ArrayList<>();
        dijkstraShortestPath.getPath(source.getName(), target.getName())
                .getVertexList()
                .forEach(f ->
                        result.addAll(getStations()
                                .stream()
                                .filter(it -> f.equals(it.getName()))
                                .collect(Collectors.toList()))
                );

        return result;
    }

    private void InitializeVertexAndEdge() {
        getStations().forEach(it -> graph.addVertex(it.getName()));
        sections.forEach(it ->
                graph.setEdgeWeight(graph.addEdge(it.getUpStation().getName(), it.getDownStation().getName()), it.getIntegerDistance())
        );
    }

    private void validateLineStation(Station source, Station target) {
        boolean isExistSource = getStations()
                .stream()
                .anyMatch(it -> it == source);
        boolean isExistTarget = getStations()
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

        return sections
                .stream()
                .filter(it ->
                        (dijkstra.contains(it.getUpStation()) && dijkstra.containsAll(Arrays.asList(it.getUpStation(), it.getDownStation())))
                )
                .distinct()
                .mapToInt(Section::getIntegerDistance)
                .sum();
    }

    private List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Set<Station> result = new LinkedHashSet<>();
        sections.forEach(f -> {
            result.add(f.getUpStation());
            result.add(f.getDownStation());
        });
        return new ArrayList<>(result);
    }


}
