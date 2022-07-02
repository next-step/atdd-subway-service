package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;
    private Line line;
    private int distance;
    public SectionEdge(Section section) {
        this.section = section;
        this.line = section.getLine();
        this.distance = section.getDistance();
    }


    public Station getSource() {
        return section.getUpStation();
    }


    public Station getTarget() {
        return section.getDownStation();
    }

    public int getDistance() {
        return distance;
    }

    public Fare getFare() {
        return line.getFare();
    }
}
