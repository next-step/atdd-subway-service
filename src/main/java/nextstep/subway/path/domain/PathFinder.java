package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph =
                new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        registerLines(routeGraph, lines);
        dijkstraShortestPath = new DijkstraShortestPath(routeGraph);
    }

    private void registerLines(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, List<Line> lines) {
        for (Line line : lines) {
            addVertexes(routeGraph, line);
            addEdges(routeGraph, line);
        }
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Line line) {
        line.getStations().stream()
                .forEach(station -> routeGraph.addVertex(station));
    }

    private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Line line) {
        line.getSections().stream()
                .forEach(section -> routeGraph.setEdgeWeight(addEdge(routeGraph, section), section.getDistance().value()));
    }

    private DefaultWeightedEdge addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Section section) {
        return routeGraph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateInputStations(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateResult(path);
        return Path.from(path);
    }

    private void validateResult(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 수 없습니다.");
        }
    }

    private void validateInputStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("[ERROR] 경로탐색 시 출발역과 도착역이 같을 수 없습니다.");
        }
    }
}
