package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class JgraphtPathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph;
    private List<SectionEdge> edges;

    public JgraphtPathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(SectionEdge.class);
        initGraph(lines);
    }

    private void initGraph(List<Line> lines) {
        for (Line line : lines) {
            addVertexes(line.getStations());
            addEdges(line.getSections());
        }
    }

    private void addVertexes(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(List<Section> sections) {
        sections.forEach(section -> {
            SectionEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        });
    }

    public Path getPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        this.edges = path.getEdgeList();
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    public boolean isSameVertex(Station source, Station target) {
        return source.equals(target);
    }

    public boolean isNotContainsVertex(Station source, Station target) {
        Set<Station> stations = graph.vertexSet();
        return !(stations.contains(source) && stations.contains(target));
    }

    public boolean isNotConnectable(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Objects.isNull(dijkstraShortestPath.getPath(source, target));
    }

    public List<SectionEdge> getEdges() {
        return edges;
    }
}
