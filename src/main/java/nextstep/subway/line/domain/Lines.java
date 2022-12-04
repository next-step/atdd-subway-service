package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.station.domain.Station;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public void addVertexAndEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.forEach(line -> line.addVertexAndEdge(graph));
    }

    public List<Line> getList() {
        return Collections.unmodifiableList(lines);
    }
}
