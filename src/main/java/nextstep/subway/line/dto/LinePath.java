package nextstep.subway.line.dto;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LinePath {
    private final List<Station> vertex;
    private final List<DefaultWeightedEdge> edge;
    private final double weight;

    public LinePath(List<Station> vertex, List<DefaultWeightedEdge> edge, double weight) {
        this.vertex = new LinkedList<>(vertex);
        this.edge = new LinkedList<>(edge);
        this.weight = weight;
    }

    public List<Station> getVertex() {
        return Collections.unmodifiableList(vertex);
    }

    public List<DefaultWeightedEdge> getEdge() {
        return Collections.unmodifiableList(edge);
    }

    public double getWeight() {
        return weight;
    }
}
