package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addAllVertex(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.stations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(this::addVertex);
    }

    public void addAllEdge(List<Line> lines) {
        lines.forEach(line -> line.sections()
             .forEach(section -> addSectionEdge(section, line)));
    }

    private void addSectionEdge(Section section, Line line) {
        SectionEdge sectionEdge = new SectionEdge(section, line.getId());
        addEdge(section.upStation(), section.downStation(), sectionEdge);
        setEdgeWeight(sectionEdge, section.distance());
    }
}
