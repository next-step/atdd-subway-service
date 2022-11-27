package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    private final List<Line> lines;

    public SubwayGraph(final Class<? extends DefaultWeightedEdge> edgeClass, final List<Line> lines) {
        super(edgeClass);
        this.lines = lines;
        initializeVertex();
        initializeEdge();
    }

    private void initializeVertex() {
        lines.stream()
                .flatMap(sections -> sections.getStations().stream())
                .forEach(this::addVertex);
    }

    private void initializeEdge() {
        lines.stream()
                .flatMap(sections -> sections.getSections().stream())
                .forEach(this::setEdgeWeight);
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return addEdge(section.upStation(), section.downStation());
    }

    private void setEdgeWeight(final Section section) {
        setEdgeWeight(addEdge(section), section.getDistance());
    }
}
