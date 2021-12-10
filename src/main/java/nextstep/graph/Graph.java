package nextstep.graph;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph<T> {

    private final WeightedMultigraph<T, DefaultWeightedEdge> graph =
        new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private DijkstraShortestPath<T, DefaultWeightedEdge> dijkstraShortestPath;

    public Graph(List<T> vertexes, List<GraphEdge<T>> edges) {
        addVertexes(vertexes);
        addEdges(edges);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<T> getShortestPathList(T source, T target) {
        try {
            return getShortestPath(source, target).getVertexList();
        } catch (VertexNotConnectedException e) {
            throw new IllegalArgumentException("출발역과 도착역이 이어진 경로가 없습니다.");
        }
    }

    public double getShortestPathWeight(T source, T target) {
        try {
            return getShortestPath(source, target).getWeight();
        } catch (VertexNotConnectedException e) {
            throw new IllegalArgumentException("출발역과 도착역이 이어진 경로가 없습니다.");
        }
    }

    private GraphPath<T, DefaultWeightedEdge> getShortestPath(T source, T target) {
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
            .orElseThrow(VertexNotConnectedException::new);
    }

    private void addVertexes(List<T> vertexes) {
        vertexes.forEach(graph::addVertex);
    }

    private void addEdges(List<GraphEdge<T>> edges) {
        for (GraphEdge<T> edge : edges) {
            graph.setEdgeWeight(graph.addEdge(edge.getSource(), edge.getTarget()),
                edge.getWeight());
        }
    }

}
