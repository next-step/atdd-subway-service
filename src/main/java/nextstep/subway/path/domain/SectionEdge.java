package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.wrapper.AdditionalFare;
import nextstep.subway.line.domain.wrapper.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Station getSourceVertex() {
        return section.getUpStation();
    }

    public Station getTargetVertex() {
        return section.getDownStation();
    }

    public int getEdgeWeight() {
        Distance sectionDistance = section.getDistance();
        return sectionDistance.getDistance();
    }

    public AdditionalFare getSectionLineAdditionalFare() {
        Line line = section.getLine();
        return line.getAdditionalFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEdge that = (SectionEdge) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }
}
