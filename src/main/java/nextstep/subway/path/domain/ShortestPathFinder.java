package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public final class ShortestPathFinder {

    private final ShortestPathAlgorithm<Station, DefaultEdge> shortestPath;

    private ShortestPathFinder(Lines lines) {
        validateLines(lines);
        shortestPath = shortestPathAlgorithm(lines);
    }

    public static ShortestPathFinder from(Lines lines) {
        return new ShortestPathFinder(lines);
    }

    public Path path(Station source, Station target) {
        validateStations(source, target);
        GraphPath<Station, DefaultEdge> path = validPath(source, target);
        return Path.of(Stations.from(path.getVertexList()), Distance.from(path.getWeight()));
    }

    private GraphPath<Station, DefaultEdge> validPath(Station source, Station target) {
        try {
            return shortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(
                String.format("출발역(%s) 과 도착역(%s)의 경로를 조회할 수 없습니다.", source, target), e);
        }
    }

    private ShortestPathAlgorithm<Station, DefaultEdge> shortestPathAlgorithm(Lines lines) {
        return new DijkstraShortestPath<>(stationGraph(lines));
    }

    private Graph<Station, DefaultEdge> stationGraph(Lines lines) {
        WeightedGraph<Station, DefaultEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : lines.stationList()) {
            graph.addVertex(station);
        }
        for (Section section : lines.sectionList()) {
            graph.setEdgeWeight(
                graph.addEdge(section.upStation(), section.downStation()), section.distanceValue()
            );
        }
        return graph;
    }

    private void validateStations(Station source, Station target) {
        Assert.notNull(source, "경로를 조회할 출발역은 null일 수 없습니다.");
        Assert.notNull(target, "경로를 조회할 도착역은 null일 수 없습니다.");
        if (source.equals(target)) {
            throw new InvalidDataException(
                String.format("출발역과 도착역을 동일(%s)하게 경로를 조회할 수 없습니다.", source));
        }
    }

    private void validateLines(Lines lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("최단 경로를 조회할 노선들이 비어있을 수 없습니다.");
        }
    }
}
