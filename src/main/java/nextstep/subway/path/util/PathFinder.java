package nextstep.subway.path.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinder {
    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder() {
    }

    public static PathResponse computePath(Collection<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 같을 수 없습니다.");
        }
        initializeGraph(lines);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = getGraphPath(source, target);
        final List<StationResponse> stations = getStationsOnPath(graphPath);
        final int distance = (int)graphPath.getWeight();

        return new PathResponse(stations, distance);
    }

    private static void initializeGraph(final Collection<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Line line : lines) {
            setStationsAsVertex(line);
            setSectionsAsEdge(line);
        }
    }

    private static GraphPath<Station, DefaultWeightedEdge> getGraphPath(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> graphPath =
            new DijkstraShortestPath<>(graph).getPath(source, target);
        if (graphPath == null) {
            throw new BadRequestException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return graphPath;
    }

    private static List<StationResponse> getStationsOnPath(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return graphPath.getVertexList().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private static void setStationsAsVertex(final Line line) {
        for (final Station station : line.extractAllStations()) {
            graph.addVertex(station);
        }
    }

    private static void setSectionsAsEdge(final Line line) {
        for (final Section section : line.getSections()) {
            final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
