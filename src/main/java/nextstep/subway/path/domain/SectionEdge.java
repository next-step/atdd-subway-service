package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Station source;
    private final Station target;
    private final Distance weight;

    public SectionEdge(final Section section) {
        this.section = section;
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.weight = section.getDistance();
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public double getWeight() {
        return weight.getDistance();
    }

    public Section getSection() {
        return section;
    }

}
