package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeigthedEdge extends DefaultWeightedEdge {
    private Station upStation;
    private Station downStation;
    private int surcharge;
    private Distance distance;

    protected SectionWeigthedEdge() {
    }

    public SectionWeigthedEdge(Section section) {
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.surcharge = section.getLine().getSurcharge();
        this.distance = section.getDistance();
    }

    public int distanceValue() {
        return distance.getDistance();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getSurcharge() {
        return surcharge;
    }

    public Distance getDistance() {
        return distance;
    }
}
