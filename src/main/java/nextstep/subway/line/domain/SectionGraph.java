package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SectionGraph {
    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SectionGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public void addVertices(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    public void addEdges(List<Section> sections) {
        sections.forEach(section -> addEdgeWith(addEdge(section, graph), section.getDistance(), graph));
    }

    private void addEdgeWith(DefaultWeightedEdge edge, int weight, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        graph.setEdgeWeight(edge, weight);
    }

    private DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public DijkstraShortestPath getPath() {
        return new DijkstraShortestPath(graph);
    }
}
