package nextstep.subway.path;

import nextstep.subway.line.domain.AdditionalFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    private SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    public Station getSourceVertex() {
        return section.getUpStation();
    }

    public Station getTargetVertex() {
        return section.getDownStation();
    }

    public int getEdgeWeight() {
        return section.getDistance();
    }

    public AdditionalFare getSectionAdditionalFare() {
        Line line = section.getLine();
        return line.getAdditionalFare();
    }
}
