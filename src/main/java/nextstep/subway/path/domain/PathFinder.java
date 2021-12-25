package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.NotConnectedStationsException;
import nextstep.subway.path.exception.NotFoundStationsInLineException;
import nextstep.subway.path.exception.SameStartEndStationException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;
    private final DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        for (Line line : lines) {
            addStationAndSection(line);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStationAndSection(Line line) {
        for (Section section : line.getSections()) {
            SectionWeightedEdge edge = SectionWeightedEdge.of(section);
            graph.addVertex(edge.getUpStation());
            graph.addVertex(edge.getDownStation());
            graph.addEdge(edge.getUpStation(), edge.getDownStation(), edge);
            graph.setEdgeWeight(edge, edge.getDistance());
        }
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    public GraphPath<Station, SectionWeightedEdge> findShortestPath(Station source, Station target) {
        isSameSourceAndTarget(source, target);
        notExistSourceAndTarget(source, target);

        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new NotConnectedStationsException("출발역과 도착역이 연결되지 않았습니다."));
    }

    private void isSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameStartEndStationException();
        }
    }

    private void notExistSourceAndTarget(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new NotFoundStationsInLineException("출발역이나 도착역이 존재하지 않습니다.");
        }
    }
}
