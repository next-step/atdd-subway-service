package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {

    public SubwayGraph(List<Line> lines) {
        super(SectionEdge.class);
        initGraph(lines);
    }

    private void initGraph(List<Line> lines) {
        lines.stream()
            .map(Line::getSections)
            .forEach(this::setAllEdgeWeight);
    }

    private void setAllEdgeWeight(Sections sections) {
        sections.getSections()
            .stream()
            .map(SectionEdge::of)
            .forEach(sectionEdge -> {
                addVertex(sectionEdge.getUpStation());
                addVertex(sectionEdge.getDownStation());
                addEdge(sectionEdge.getUpStation(), sectionEdge.getDownStation(), sectionEdge);
                setEdgeWeight(sectionEdge, sectionEdge.getDistance());
            });
    }
}
