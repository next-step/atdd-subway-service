package nextstep.subway.path.util;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class GraphGenerator {
    private Lines lines;
    private WeightedMultigraph graph;

    public GraphGenerator(Lines lines) {
        this.lines = lines;
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        generateGraph();
    }

    private void generateGraph() {
        addVertex();
        addEdges();
    }

    private void addVertex() {
        List<Station> stations = lines.getStations();
        stations.forEach(graph::addVertex);
    }

    private void addEdges() {
        lines.getLines()
                .forEach(v -> addEdges(graph, v));
    }

    private void addEdges(WeightedMultigraph graph, Line line) {
        line.getSections()
                .forEach(v -> setEdges(v, graph));
    }

    private void setEdges(Section section, WeightedMultigraph graph) {
        SectionEdge sectionEdge = new SectionEdge(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, section.getDistance());
    }

    public WeightedMultigraph getGraph() {
        return graph;
    }
}
