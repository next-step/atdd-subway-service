package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SectionGraph implements PathFinderGraph{
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SectionGraph() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    @Override
    public void addVertices(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    @Override
    public void addEdges(List<Section> sections) {
        sections.forEach(section -> addEdgeWith(addEdge(section, graph), section.getDistance(), graph));
    }

    @Override
    public void addEdgeWith(DefaultWeightedEdge edge, int weight, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        graph.setEdgeWeight(edge, weight);
    }

    @Override
    public DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    @Override
    public DijkstraShortestPath getPath() {
        return new DijkstraShortestPath(graph);
    }
}
