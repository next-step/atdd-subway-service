package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {

    private final List<Line> lines;

    public SubwayGraph(List<Line> lines) {
        super(DefaultWeightedEdge.class);
        this.lines = lines;
        addVertex();
        setEdgeWeight();
    }

    private void addVertex() {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .forEach(this::addVertex);
    }

    private void setEdgeWeight() {
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(this::setEdgeWeight);
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return addEdge(section.getUpStation(), section.getDownStation());
    }

    private void setEdgeWeight(Section section) {
        setEdgeWeight(addEdge(section), section.getDistance());
    }
}
