package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.exception.path.PathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LinePathSearch {

    private WeightedMultigraph<Station, SectionEdge> weightedMultigraph;
    private DijkstraShortestPath<Station, SectionEdge> dijkstra;

    private LinePathSearch(List<Line> lines) {
        this.weightedMultigraph = settingGraph(lines);
        this.dijkstra = new DijkstraShortestPath<>(this.weightedMultigraph);
    }

    private WeightedMultigraph<Station, SectionEdge> settingGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addAllToGraph(lines, graph);

        return graph;
    }

    private void addAllToGraph(List<Line> lines, WeightedMultigraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(line -> line.getSections().stream())
            .forEach(section -> {
                SectionEdge edge = SectionEdge.of(section);
                graph.addVertex(section.getUpStation());
                graph.addVertex(section.getDownStation());
                graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                graph.setEdgeWeight(edge, section.getDistance());
            });
    }

    public static LinePathSearch of(List<Line> lines) {
        return new LinePathSearch(lines);
    }

    public Path searchPath(Station source, Station target) {
        validataionStation(source, target);
        GraphPath<Station, SectionEdge> path = dijkstra.getPath(source, target);
        if (path == null) {
            throw new PathException(PathException.NOT_CONNECTED);
        }
        return Path.of(path);
    }

    private void validataionStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathException(PathException.SAME_STATION);
        }

        if (!weightedMultigraph.containsVertex(source) || !weightedMultigraph.containsVertex(target)) {
            throw new PathException(PathException.NO_REGISTRATION);
        }
    }

}
