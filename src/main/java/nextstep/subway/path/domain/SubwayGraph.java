package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private SubwayGraph(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addStationsToVertex(graph, line);
            addSectionsToEdgeWithWeight(graph, line);
        }
    }

    private void addStationsToVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void addSectionsToEdgeWithWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public static SubwayGraph of(List<Line> lines) {
        return new SubwayGraph(lines);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
