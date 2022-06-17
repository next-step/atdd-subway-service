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
    public void addGraphComposition(Lines lines) {
        lines.registerPath(this);
    }

    @Override
    public DijkstraShortestPath getPath(Lines lines) {
        addGraphComposition(lines);
        return new DijkstraShortestPath(graph);
    }

    public void addVertices(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    public void addEdgeAndWeight(List<Section> sections) {
        sections.forEach(section -> {
            SectionGraphEdge sectionEdge = SectionGraphEdge.of(section);
            addEdge(section, graph, sectionEdge);
            addEdgeWeight(section.getDistance(), graph, sectionEdge);
        });
    }

    private void addEdgeWeight(int weight, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge edge) {
        graph.setEdgeWeight(edge, weight);
    }

    private void addEdge(Section section, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge sectionGraphEdge) {
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionGraphEdge);
    }
}
