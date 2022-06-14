package nextstep.subway.path.vo;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Station upStation;
    private final Station downStation;
    private final Line line;
    private final int distance;

    public SectionEdge(Section section) {
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.line = section.getLine();
        this.distance = section.getDistance();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
