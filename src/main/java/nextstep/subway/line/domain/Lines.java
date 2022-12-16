package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lines {
    private List<Line> lines;

    public Lines() {}

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public boolean isEmpty() {
        return this.lines.isEmpty();
    }

    public Fare getMaxAddedFare() {
        return this.lines.stream()
                .map(Line::getAddedFare)
                .max(Comparator.comparing(Fare::value))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_FOUND.getMessage()));
    }

    public WeightedMultigraph<Station, Section> getGraph(WeightedMultigraph<Station, Section> graph) {
        this.lines.forEach(line -> {
            addVertex(graph, line);
            addEdgeAndSetEdgeWeight(graph, line);
        });
        return graph;
    }

    private void addVertex(WeightedMultigraph<Station, Section> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void addEdgeAndSetEdgeWeight(WeightedMultigraph<Station, Section> graph, Line line) {
        line.getSections().forEach(
                section -> {
                    graph.addEdge(section.getUpStation(), section.getDownStation(), section);
                    graph.setEdgeWeight(section, section.getDistance().value());
                });
    }

    public List<Line> values() {
        return Collections.unmodifiableList(lines);
    }
}
