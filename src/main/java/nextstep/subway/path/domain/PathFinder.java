package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {

    DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(Lines lines) {
        return new PathFinder(initGraph(lines));
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateDuplicateStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateNotConnectStation(graphPath);
        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(lines, graph);
        addEdgeAndWeight(lines, graph);
        return graph;
    }

    private static void addVertex(Lines lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = lines.getStations();
        stations.forEach(graph::addVertex);
    }

    private static void addEdgeAndWeight(Lines lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Section> sections = lines.getSections();
        sections.forEach(section -> {
            DefaultWeightedEdge defaultWeightedEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(defaultWeightedEdge, section.getDistance());
        });
    }

    private void validateDuplicateStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new BadRequestException("출발역과 도착역이 같은 경우 최단 거리를 구할 수 없습니다.");
        }
    }

    private void validateNotConnectStation(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new BadRequestException("출발역과 도착역이 연결되어있지 않은 경우 조회할 수 없습니다.");
        }
    }
}
