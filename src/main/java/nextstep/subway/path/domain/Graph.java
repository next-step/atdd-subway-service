package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {
    private WeightedMultigraph<Station, SectionEdge> graph;

    public Graph(List<Line> lines) {
        generateGraph(lines);
    }

    private void generateGraph(List<Line> lines){
        graph = new WeightedMultigraph(SectionEdge.class);

        lines.stream()
                .forEach(line -> line.getSections().stream()
                        .forEach(section -> {

                            SectionEdge sectionEdge = new SectionEdge(section);
                            graph.addVertex(section.getUpStation());
                            graph.addVertex(section.getDownStation());
                            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                            graph.setEdgeWeight(sectionEdge, section.getDistance().value());
                        }));
    }

    public WeightedMultigraph<Station, SectionEdge> getGraph() {
        return graph;
    }
}
