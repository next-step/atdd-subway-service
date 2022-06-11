package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class PathFinder {

    private final ShortestPathAlgorithm<Station, SectionEdge> shortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addStations(graph, lines);
        addDistance(graph, lines);
        shortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStations(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
        lines.stream()
             .map(Line::getStations)
             .flatMap(Collection::stream)
             .distinct()
             .forEach(graph::addVertex);
    }

    private void addDistance(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
        lines.stream()
             .map(Line::getSections)
             .flatMap(Collection::stream)
             .forEach(section -> {
                 SectionEdge edge = new SectionEdge(section);
                 graph.addEdge((Station) edge.getSource(), (Station) edge.getTarget(), edge);
                 graph.setEdgeWeight(edge, edge.getWeight());
             });
    }

    public Path findShortestPath(Station source, Station target) {
        validateSameStation(source, target);
        try {
            GraphPath<Station, SectionEdge> graphPath = shortestPath.getPath(source, target);
            return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), graphPath.getEdgeList());
        } catch (IllegalArgumentException e) {
            throw new InvalidPathFindException("목적지가 출발지와 연결되어 있지 않습니다.");
        }
    }

    private void validateSameStation(Station source, Station target) {
        requireNonNull(source, "source");
        requireNonNull(target, "target");
        if (source.equals(target)) {
            throw new InvalidPathFindException("출발지와 목적지가 같을 수 없습니다.");
        }
    }
}
