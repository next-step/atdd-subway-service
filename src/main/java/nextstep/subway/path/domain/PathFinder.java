package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final DijkstraShortestPath<Station, SectionEdge> shortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = getInitializedGraph(lines);
        shortestPath = new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, SectionEdge> getInitializedGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        for (Line line : lines) {
            initGraph(graph, line);
        }
        return graph;
    }

    private void initGraph(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        Sections sections = line.getSections();
        sections.getStations().forEach(graph::addVertex);
        sections.getList().forEach(section -> {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new SectionEdge(section));
        });
    }

    public List<Station> findPathStations(Station source, Station destination) {
        return findShortestPath(source, destination).getVertexList();
    }

    public int findPathLength(Station source, Station destination) {
        return (int) findShortestPath(source, destination).getWeight();
    }

    private GraphPath<Station, SectionEdge> findShortestPath(Station source, Station destination) {
        validatePaths(source, destination);
        GraphPath<Station, SectionEdge> path = shortestPath.getPath(source, destination);
        if (path == null) {
            throw new InvalidPathException("존재하지 않는 경로입니다.");
        }
        return path;
    }

    private void validatePaths(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new InvalidPathException("출발역과 도착역은 달라야 합니다.");
        }
    }
}
