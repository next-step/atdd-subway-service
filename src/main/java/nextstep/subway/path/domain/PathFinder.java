package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.exception.WrongPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionWeightEdge> graph;

    public PathFinder(Line... lines) {
        this(Arrays.asList(lines));
    }

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, SectionWeightEdge> graph = new WeightedMultigraph<>(
            SectionWeightEdge.class);
        addVertex(graph, lines);
        putEdgeWeight(graph, lines);
        this.graph = graph;
    }

    private void addVertex(WeightedMultigraph<Station, SectionWeightEdge> graph,
        List<Line> lines) {
        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .forEach(graph::addVertex);
    }

    private void putEdgeWeight(WeightedMultigraph<Station, SectionWeightEdge> graph,
        List<Line> lines) {
        lines.stream()
            .map(Line::getSections)
            .map(Sections::getSections)
            .flatMap(Collection::stream)
            .forEach(section -> graph.addEdge(section.getUpStation(), section.getDownStation(),
                new SectionWeightEdge(section)));
    }

    public List<Station> shortestPath(Station from, Station to) {
        GraphPath<Station, SectionWeightEdge> path = getPath(from, to);

        return path.getVertexList();
    }

    public int getShortestDistance(Station from, Station to) {
        GraphPath<Station, SectionWeightEdge> path = getPath(from, to);

        return (int) path.getWeight();
    }

    private GraphPath<Station, SectionWeightEdge> getPath(
        Station from, Station to) {
        validateArgument(from, to);

        DijkstraShortestPath<Station, SectionWeightEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);

        GraphPath<Station, SectionWeightEdge> path = dijkstraShortestPath.getPath(from, to);
        validatePath(path);
        return path;
    }

    private void validateArgument(Station from, Station to) {
        if (from.equals(to)) {
            throw new WrongPathException("출발역과 도착역이 동일합니다.");
        }

        if (!graph.containsVertex(from) || !graph.containsVertex(to)) {
            throw new WrongPathException("존재하지 않는 역입니다.");
        }
    }

    private void validatePath(GraphPath<Station, SectionWeightEdge> path) {
        if (path == null) {
            throw new WrongPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
