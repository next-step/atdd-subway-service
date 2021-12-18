package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public SectionWeightedEdge(Section section) {
        upStation = section.getUpStation();
        downStation = section.getDownStation();
        distance = section.getDistance();
    }

    public static SectionWeightedEdge of(Section section) {
        return new SectionWeightedEdge(section);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
