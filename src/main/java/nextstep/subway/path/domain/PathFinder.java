package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PathFinder {
    public static PathResult findShortCut(Set<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStationToGraph(lines, graph);
        linkAllSections(lines, graph);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .map(path -> new PathResult(path.getVertexList(), path.getWeight()))
                .orElseGet(PathResult::emptyPath);
    }

    private static void linkAllSections(Set<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Set<Section> sections = getAllSections(lines);
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private static void addStationToGraph(Set<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Set<Station> stations = getAllStations(lines);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static Set<Station> getAllStations(Set<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
    }

    private static Set<Section> getAllSections(Set<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toSet());
    }
}
