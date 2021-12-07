package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public final class SectionEdge extends DefaultWeightedEdge {
    private final Station source;
    private final Station target;
    private final Distance distance;

    private SectionEdge(Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.distance = section.getDistance();
    }

    public static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    @Override
    public Station getSource() {
        return source;
    }

    @Override
    public Station getTarget() {
        return target;
    }

    public Integer getDistance() {
        return distance.get();
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
            "source=" + source +
            ", target=" + target +
            ", distance=" + distance +
            '}';
    }
}
