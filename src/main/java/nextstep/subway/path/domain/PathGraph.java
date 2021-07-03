package nextstep.subway.path.domain;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathGraph {
    private final WeightedMultigraph<PathVertex, DefaultWeightedEdge> graph;

    public PathGraph() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public void setPathInfoBy(GraphEdgeWeight graphEdgeWeight) {
        this.graph.addVertex(graphEdgeWeight.getSourceVertex());
        this.graph.addVertex(graphEdgeWeight.getTargetVertex());
        DefaultWeightedEdge weightedEdge = graph.addEdge(graphEdgeWeight.getSourceVertex(),
                graphEdgeWeight.getTargetVertex());
        this.graph.setEdgeWeight(weightedEdge, graphEdgeWeight.getWeight());
    }

    public Graph getGraph() {
        return this.graph;
    }
}
