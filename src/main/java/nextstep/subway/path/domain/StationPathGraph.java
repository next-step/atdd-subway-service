package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class StationPathGraph extends WeightedMultigraph<Station, SectionEdge> {

    public StationPathGraph(List<Line> lines) {
        super(SectionEdge.class);
        lines.forEach(this::addLine);
    }

    private void addLine(Line line) {
        addStation(line.getStations());
        addSection(line.getSections());
    }

    private void addSection(Sections sections) {
        sections.getSections().forEach(this::addEdgeWeight);
    }

    private void addStation(List<Station> stations) {
        stations.forEach(this::addVertex);
    }

    private void addEdgeWeight(Section section) {
        SectionEdge sectionEdge = addEdge(section.getUpStation(), section.getDownStation());
        sectionEdge.addSection(section);

        setEdgeWeight(sectionEdge, section.getDistance().getValue());
    }

}
