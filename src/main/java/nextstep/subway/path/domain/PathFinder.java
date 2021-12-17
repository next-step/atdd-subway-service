package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinder {
    private static WeightedMultigraph<Station, Section> graph;

    private PathFinder() {
    }

    public static PathResponse computePath(
        final Collection<Line> lines, final Station source, final Station target, final int memberAge
    ) {
        validate(lines, source, target);
        initializeGraph(lines);
        final GraphPath<Station, Section> graphPath = getGraphPath(source, target);
        final List<StationResponse> stations = getStationsOnPath(graphPath);
        final int distance = (int)graphPath.getWeight();
        final Set<Line> pathLines = getLinesOnPath(graphPath);
        final int fare = FareCalculator.calculateFare(pathLines, distance, memberAge);

        return new PathResponse(stations, distance, fare);
    }

    private static void validate(final Collection<Line> lines, final Station source, final Station target) {
        if (lines.isEmpty()) {
            throw new BadRequestException("노선이 없을 때 경로를 조회할 수 없습니다.");
        }
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private static void initializeGraph(final Collection<Line> lines) {
        graph = new WeightedMultigraph<>(Section.class);
        for (final Line line : lines) {
            setStationsAsVertex(line);
            setSectionsAsEdge(line);
        }
    }

    private static GraphPath<Station, Section> getGraphPath(final Station source, final Station target) {
        final GraphPath<Station, Section> graphPath =
            new DijkstraShortestPath<>(graph).getPath(source, target);
        if (graphPath == null) {
            throw new BadRequestException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return graphPath;
    }

    private static List<StationResponse> getStationsOnPath(final GraphPath<Station, Section> graphPath) {
        return graphPath.getVertexList().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private static Set<Line> getLinesOnPath(final GraphPath<Station, Section> graphPath) {
        return graphPath.getEdgeList().stream()
            .map(Section::getLine)
            .collect(Collectors.toSet());
    }

    private static void setStationsAsVertex(final Line line) {
        for (final Station station : line.extractAllStations()) {
            graph.addVertex(station);
        }
    }

    private static void setSectionsAsEdge(final Line line) {
        for (final Section section : line.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), section);
            graph.setEdgeWeight(section, section.getDistance());
        }
    }
}
