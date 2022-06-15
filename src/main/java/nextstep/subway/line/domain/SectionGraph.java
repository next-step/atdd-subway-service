package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SectionGraph implements PathFinderGraph {
    private final WeightedMultigraph<Station, SectionGraphEdge> graph;

    public SectionGraph() {
        this.graph = new WeightedMultigraph(SectionGraphEdge.class);
    }

    @Override
    public void addVertices(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    @Override
    public void addEdges(List<Section> sections) {
        sections.forEach(section -> {
            SectionGraphEdge sectionEdge = SectionGraphEdge.of(section);
            addEdge(section, graph, sectionEdge);
            addEdgeWith(section.getDistance(), graph, sectionEdge);
        });
    }

    @Override
    public void addEdgeWith(int weight, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge edge) {
        graph.setEdgeWeight(edge, weight);
    }

    @Override
    public void addEdge(Section section, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge sectionGraphEdge) {
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionGraphEdge);
    }

    @Override
    public DijkstraShortestPath getPath() {
        return new DijkstraShortestPath(graph);
    }
}
