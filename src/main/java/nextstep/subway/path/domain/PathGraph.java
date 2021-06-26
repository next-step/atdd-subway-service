package nextstep.subway.path.domain;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathGraph() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public void setPathInfoBy(Section section) {
        this.graph.addVertex(section.getUpStation());
        this.graph.addVertex(section.getDownStation());
        DefaultWeightedEdge weightedEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
        this.graph.setEdgeWeight(weightedEdge, section.getDistance());
    }

    public Graph getGraph() {
        return this.graph;
    }
}
