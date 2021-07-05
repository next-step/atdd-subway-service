package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Graph {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph() {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public void build(List<Line> lines) {
        lines.forEach(line -> {
                    addVertexes(line);
                    setEdgeWeights(line);
                });
    }

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        build(lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        verifyAvailable(graphPath);

        List<Station> stations = graphPath.getVertexList();
        ShortestDistance distance = new ShortestDistance((int) dijkstraShortestPath.getPathWeight(source, target));
        return new Path(stations, distance);
    }

    private List<DefaultWeightedEdge> getEdges(List<Station> stations) {
        List<DefaultWeightedEdge> edges = new ArrayList<>();
        for (int i = 1; i < stations.size(); i++) {
            edges.add(graph.getEdge(stations.get(i-1), stations.get(i)));
        }
        return edges;
    }

    public Set<Station> getVertexes() {
        return graph.vertexSet();
    }

    private void setEdgeWeights(Line line) {
        line.getSections()
                .forEach(section -> {
                    DefaultWeightedEdge edge = new DefaultWeightedEdge();
                    graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                    graph.setEdgeWeight(edge, section.getWeight());
                });
    }

    private void addVertexes(Line line) {
        line.findStationsOrderUpToDown()
                .forEach(station -> graph.addVertex(station));
    }

    private void verifyAvailable(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
