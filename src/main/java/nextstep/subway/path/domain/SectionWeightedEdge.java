package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;

    SectionWeightedEdge(Section section) {
        this.section = section;
    }

    Station getUpStation() {
        return section.getUpStation();
    }

    Station getDownStation() {
        return section.getDownStation();
    }

    int getDistance() {
        return section.getDistance();
    }
}
