package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph(List<Line> lines) {
        generateGraph(lines);
    }

    private void generateGraph(List<Line> lines){
        graph = new WeightedMultigraph(org.jgrapht.graph.DefaultWeightedEdge.class);

        lines.stream().forEach(line -> line.getStations().stream()
                .forEach(station -> graph.addVertex(station)));
        lines.stream().forEach(line -> line.getSections().stream()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value())));
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
