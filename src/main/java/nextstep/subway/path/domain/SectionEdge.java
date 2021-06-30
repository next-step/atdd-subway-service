package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Station upStation;
    private final Station downStation;
    private final int charge;

    public SectionEdge(Station startStation, Station endStation, int charge) {
        this.upStation = startStation;
        this.downStation = endStation;
        this.charge = charge;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(), section.getLine().getCharge());
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getCharge() {
        return charge;
    }
}
