package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void makeGraph(WeightedMultigraph<Long, SectionEdge> graph) {
        lines.forEach(line-> {
            addVertex(graph, line);
            setEdgeWeight(graph, line);
        });
    }

    private void addVertex(WeightedMultigraph<Long, SectionEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station.getId());
        }
    }

    private void setEdgeWeight(WeightedMultigraph<Long, SectionEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            SectionEdge edge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            graph.setEdgeWeight(edge, section.getDistance());
            edge.addSection(section);
        }
    }

}
