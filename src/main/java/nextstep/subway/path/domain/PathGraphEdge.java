package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathGraphEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Line line;
    private final Distance distance;

    public PathGraphEdge(Section section) {
        this.section = section;
        this.line = section.getLine();
        this.distance = section.getDistance();
    }


    public double getWeight() {
        return distance.value();
    }


    public Object getSource() {
        return section.getUpStation();
    }


    public Object getTarget() {
        return section.getDownStation();
    }

    public Fare getLineFare() {
        return line.getFare();
    }

}
