package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionWeightedEdge(Section section) {
        this.section = section;
    }

    static public SectionWeightedEdge of(Section section) {
        return new SectionWeightedEdge(section);
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public Station getDownStation() {
        return section.getDownStation();
    }

    public int getDistance() {
        return section.getDistance();
    }
}
